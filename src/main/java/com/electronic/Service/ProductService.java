package com.electronic.Service;

import com.electronic.Dto.PageableResponse;
import com.electronic.Dto.ProductDto;



public interface ProductService {

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto , String  id);

    void deleteProduct(String id);

    PageableResponse<ProductDto> findAll(int pageNumber, int pageSize, String  sortBy, String sortDir);

    ProductDto findById(String id);
    PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir);

    PageableResponse<ProductDto> searchByTitle(String subtitle,int pageNumber, int pageSize, String sortBy, String sortDir);

// create product with category
    ProductDto createWithCategory(ProductDto productDto, String categoryid);

    // update category of product
    ProductDto updateWithCategory(String productId, String categoryId);

    PageableResponse<ProductDto> getAllOfCategory(String  categoryid,int pageNumber, int pageSize, String sortBy, String sortDir);

}
