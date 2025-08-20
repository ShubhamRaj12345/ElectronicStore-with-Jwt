package com.electronic.Service.Impl;

import com.electronic.Dto.PageableResponse;
import com.electronic.Dto.ProductDto;
import com.electronic.Entity.Category;
import com.electronic.Entity.Products;
import com.electronic.HandlingException.ResourceNotFoundException;
import com.electronic.Helper.HelperClass;
import com.electronic.Repository.CategoryRepository;
import com.electronic.Repository.ProductRepository;
import com.electronic.Service.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryRepository categoryRepository;

    @Value("${product.image.path}")
    private String productImagePath;

    @Override
    public ProductDto createProduct(ProductDto productDto) {

        Products map = modelMapper.map(productDto, Products.class);

        String string = UUID.randomUUID().toString();
        map.setProductid(string);
        map.setAddedDate(new Date());

        Products save = productRepository.save(map);
        ProductDto map1 = modelMapper.map(save, ProductDto.class);
        return map1;
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String id) {
        Products products = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Products not Found With Given Id"));

        products.setTitle(productDto.getTitle());
        products.setDescription(productDto.getDescription());
        products.setPrice(productDto.getPrice());
        products.setQuantity(productDto.getQuantity());
        products.setLive(productDto.isLive());
        products.setStock(productDto.isStock());
        products.setDiscountedPrice(productDto.getDiscountedPrice());
        products.setProductImageName(productDto.getProductImageName());
        Products save = productRepository.save(products);

        return modelMapper.map(save, ProductDto.class);
    }

    @Override
    public void deleteProduct(String id) {
        Products products = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Products not Found With Given Id"));

        String fullpath = productImagePath + products.getProductImageName();

        try{
            Path path = Paths.get(fullpath);
            Files.delete(path);
        }

        catch (NoSuchFileException e){
             logger.info("No such file or directory found");
            e.printStackTrace();
        }

        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        productRepository.delete(products);
    }

    @Override
    public PageableResponse<ProductDto> findAll(int pageNumber, int pageSize, String sortBy, String sortDir) {


        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy)).descending(): (Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber, pageSize , sort);
        Page<Products> all = productRepository.findAll(pageable);
        return HelperClass.getPageableresponse(all, ProductDto.class);
    }

    @Override
    public ProductDto findById(String id) {
        Products products = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Products not Found With Given Id"));
        ProductDto map = modelMapper.map(products, ProductDto.class);
        return map;
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy)).descending(): (Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber, pageSize , sort);
        Page<Products> byLive = productRepository.findByLiveTrue(pageable);
        return HelperClass.getPageableresponse(byLive, ProductDto.class);

    }

    @Override
    public PageableResponse<ProductDto> searchByTitle( String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy)).descending(): (Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber, pageSize , sort);
        Page<Products> byLive = productRepository.findByTitleContaining( subTitle, pageable);
        return HelperClass.getPageableresponse(byLive, ProductDto.class);

    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryid) {
        Category category = categoryRepository.findById(categoryid).orElseThrow(() -> new ResourceNotFoundException("Categorys not Found With Given Id"));
        Products map = modelMapper.map(productDto, Products.class);

        String string = UUID.randomUUID().toString();
        map.setProductid(string);
        map.setAddedDate(new Date());

        map.setCategory(category);

        Products save = productRepository.save(map);
        ProductDto map1 = modelMapper.map(save, ProductDto.class);
        return map1;

    }

    @Override
    public ProductDto updateWithCategory(String productId, String categoryId) {
        // product fetch
        Products products = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Products not Found With Given Id"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Categorys not Found With Given Id"));

         products.setCategory(category);
        Products saveProducts = productRepository.save(products);
        return modelMapper.map(saveProducts, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllOfCategory(String categoryid, int pageNumber, int pageSize, String sortBy, String sortDir) {

        Category category = categoryRepository.findById(categoryid).orElseThrow(() -> new ResourceNotFoundException("Categorys not Found With Given Id"));

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy)).descending(): (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);

        Page<Products> page= productRepository.findByCategory(category ,pageable);

        return HelperClass.getPageableresponse(page, ProductDto.class);
    }
}
