package com.electronic.Service.Impl;

import com.electronic.Dto.CategoryDto;
import com.electronic.Dto.PageableResponse;
import com.electronic.Entity.Category;
import com.electronic.HandlingException.ResourceNotFoundException;
import com.electronic.Helper.HelperClass;
import com.electronic.Repository.CategoryRepository;
import com.electronic.Service.CategoryService;
import lombok.experimental.Helper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository  categoryRepository;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {

        Category map = modelMapper.map(categoryDto, Category.class);
        String string = UUID.randomUUID().toString();
        map.setCategoryId(string);
        Category save = categoryRepository.save(map);
        CategoryDto map1 = modelMapper.map(save, CategoryDto.class);
        return map1;
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {
        Category categoryNotFound = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
      // update category details
        categoryNotFound.setTitle(categoryDto.getTitle());
        categoryNotFound.setDescription(categoryDto.getDescription());
        categoryNotFound.setCoverImage(categoryDto.getCoverImage());


        Category save = categoryRepository.save(categoryNotFound);
        CategoryDto map = modelMapper.map(save, CategoryDto.class);
        return map;
    }

    @Override
    public void deleteCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not found With Given Id"));
        categoryRepository.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> findAll(int pageNumber, int pageSize, String  sortBy, String  sortDir) {

       Sort sort=  (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);

        Page<Category> all = categoryRepository.findAll(pageable);
        PageableResponse<CategoryDto> pageableresponse = HelperClass.getPageableresponse(all, CategoryDto.class);
        return pageableresponse;
    }

    @Override
    public CategoryDto findSingleCategory(String categoryId) {

     Category category=    categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not found With Given Id"));

        return modelMapper.map(category, CategoryDto.class);
    }
}
