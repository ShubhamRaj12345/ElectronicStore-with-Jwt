package com.electronic.Controllers;

import com.electronic.Dto.ApiResponseMessage;
import com.electronic.Dto.CategoryDto;
import com.electronic.Dto.PageableResponse;
import com.electronic.Dto.ProductDto;
import com.electronic.Service.CategoryService;
import com.electronic.Service.ProductService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){

        CategoryDto category = categoryService.createCategory(categoryDto);

        return  new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public  ResponseEntity<CategoryDto> updateCategory(@Valid @PathVariable String  id, @RequestBody CategoryDto categoryDto)
    {
        CategoryDto categoryDto1 = categoryService.updateCategory(categoryDto, id);

        return  new ResponseEntity<>(categoryDto1, HttpStatus.OK);
    }

    // for delete

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String id){

        categoryService.deleteCategory(id);

        ApiResponseMessage categoryDeletedSucessfully = ApiResponseMessage.builder().message("Category deleted sucessfully").sucess(true).status(HttpStatus.OK).build();

        return new ResponseEntity<>(categoryDeletedSucessfully, HttpStatus.OK);
    }
    @GetMapping("/getall")
    public  ResponseEntity<PageableResponse<CategoryDto>> getAllCategory(

            @RequestParam (value = "pageNumber" ,defaultValue = "0",required = false) int pageNumber,
            @RequestParam (value = "pageSize" , defaultValue = "10" , required = false) int pageSize,
            @RequestParam(value = "sortBy" ,defaultValue = "title", required = false ) String  sortBy,
            @RequestParam(value = "sortdir" , defaultValue = "asc" , required = false ) String  sortDir
    ){

        PageableResponse<CategoryDto> all = categoryService.findAll(pageNumber, pageSize, sortBy, sortDir);

        return new ResponseEntity<>(all,HttpStatus.OK);

    }

    // finnd single category
    @GetMapping("/getsingle/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable String id){
        CategoryDto singleCategory = categoryService.findSingleCategory(id);
        return  ResponseEntity.ok(singleCategory);
    }


    // createproductWith Category
    @PostMapping("/{categortid}/product")
    public  ResponseEntity<ProductDto> createProductWithCategory(@PathVariable String categortid,
                                                                 @RequestBody ProductDto productDto

    ){
        ProductDto withCategory = productService.createWithCategory(productDto, categortid);
        return  ResponseEntity.ok(withCategory);
    }

    // update category of products // assign category to products
    @PutMapping("/{categoryid}/products/{productId}")
    public  ResponseEntity<ProductDto> updateProductWithCategory(
            @PathVariable String categoryid,
            @PathVariable String productId
    ){
        ProductDto productDto = productService.updateWithCategory(productId, categoryid);
        return  ResponseEntity.ok(productDto);
    }

    // get products of category

    @GetMapping("/{categoryid}/products")
    public  ResponseEntity<PageableResponse<ProductDto>> updateProductWithCategory(
            @PathVariable String categoryid,
            @RequestParam (value = "pageNumber" ,defaultValue = "0",required = false) int pageNumber,
            @RequestParam (value = "pageSize" , defaultValue = "10" , required = false) int pageSize,
            @RequestParam(value = "sortBy" ,defaultValue = "title", required = false ) String  sortBy,
            @RequestParam(value = "sortdir" , defaultValue = "asc" , required = false ) String  sortDir
    ){
         PageableResponse <ProductDto> productDto = productService.getAllOfCategory( categoryid, pageNumber, pageSize,sortBy,sortDir);
        return  ResponseEntity.ok(productDto);
    }

}
