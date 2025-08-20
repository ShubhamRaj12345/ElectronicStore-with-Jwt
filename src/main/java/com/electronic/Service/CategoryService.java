package com.electronic.Service;

import com.electronic.Dto.CategoryDto;
import com.electronic.Dto.PageableResponse;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto , String  categoryId);

    void deleteCategory(String categoryId);

    PageableResponse<CategoryDto> findAll(int pageNumber, int pageSize, String  sortBy, String  sortDir);

    CategoryDto findSingleCategory(String categoryId);



}
