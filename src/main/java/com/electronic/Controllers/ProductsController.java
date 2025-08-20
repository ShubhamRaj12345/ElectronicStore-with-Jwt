package com.electronic.Controllers;

import com.electronic.Dto.*;
import com.electronic.Service.FileService;
import com.electronic.Service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/product")
public class ProductsController {

    Logger logger = LoggerFactory.getLogger(ProductsController.class);

    @Autowired
    private ProductService productService;
    @Autowired
    private FileService fileService;


    @Value("${product.image.path}")
    private  String  imagePath;

    @PostMapping("/create")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto)
    {
        ProductDto product = productService.createProduct(productDto);
        return  new ResponseEntity<>(product, HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto, @PathVariable String  id){

        ProductDto updatedproductDto1 = productService.updateProduct(productDto, id);
        return  new ResponseEntity<>(updatedproductDto1, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public  ResponseEntity<ApiResponseMessage> deleteProducts(@PathVariable String id){
        ApiResponseMessage productDeletedSucessfully = ApiResponseMessage.builder().message("Product deleted sucessfully").status(HttpStatus.OK).sucess(true).build();
        productService.deleteProduct(id);
        return new ResponseEntity<>(productDeletedSucessfully, HttpStatus.OK);
    }
    @GetMapping("/get/{id}")
    public  ResponseEntity<ProductDto> getProductById(@PathVariable String id){
        ProductDto byId = productService.findById(id);
        return  new ResponseEntity<>(byId, HttpStatus.OK);
    }

    @GetMapping("/title/{title}")
    public  ResponseEntity<PageableResponse<ProductDto>> searchByTitle(
             @PathVariable String title,
             @RequestParam(value ="pageNumber" , required = false, defaultValue = "0") int pageNumber,
             @RequestParam(value ="pageSize" , required = false, defaultValue = "10") int pageSize,
             @RequestParam(value ="pageSortBy" , required = false, defaultValue = "title") String  sortBy,
             @RequestParam(value ="pageSortDir" , required = false, defaultValue = "asc") String sortDir
    ){
        PageableResponse<ProductDto> productDtoPageableResponse = productService.searchByTitle(title, pageNumber, pageSize, sortBy, sortDir);

        return  new ResponseEntity<>(productDtoPageableResponse , HttpStatus.OK);
    }

    @GetMapping("/get")
    public  ResponseEntity<PageableResponse<ProductDto>> getAll(
            @RequestParam(value ="pageNumber" , required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value ="pageSize" , required = false, defaultValue = "10") int pageSize,
            @RequestParam(value ="pageSortBy" , required = false, defaultValue = "title") String  sortBy,
            @RequestParam(value ="pageSortDir" , required = false, defaultValue = "asc") String sortDir
    ){
        PageableResponse<ProductDto> productDtoPageableResponse = productService.findAll( pageNumber, pageSize, sortBy, sortDir);

        return  new ResponseEntity<>(productDtoPageableResponse , HttpStatus.OK);
    }


    @GetMapping("/live")
    public  ResponseEntity<PageableResponse<ProductDto>> getAllLive(
            @RequestParam(value ="pageNumber" , required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value ="pageSize" , required = false, defaultValue = "10") int pageSize,
            @RequestParam(value ="pageSortBy" , required = false, defaultValue = "title") String  sortBy,
            @RequestParam(value ="pageSortDir" , required = false, defaultValue = "asc") String sortDir
    ){
        PageableResponse<ProductDto> productDtoPageableResponse = productService.getAllLive( pageNumber, pageSize, sortBy, sortDir);

        return  new ResponseEntity<>(productDtoPageableResponse , HttpStatus.OK);
    }

    // upload image
    @PostMapping("/upload/{productId}")
    public  ResponseEntity<ImageResponse>  uploadProductImage(
            @PathVariable String productId,
            @RequestParam ("productImage")MultipartFile image
            ) throws IOException {
        String s = fileService.uploadFile(image, imagePath);
        ProductDto byId = productService.findById(productId);
        byId.setProductImageName(s);
        ProductDto productDto = productService.updateProduct(byId, productId);
        ImageResponse productImageIsSucessfullyUploded = ImageResponse.builder().imageName(productDto.getProductImageName()).message("Product image is sucessfully uploded").status(HttpStatus.OK).sucess(true).build();
        return  new ResponseEntity<>(productImageIsSucessfullyUploded,HttpStatus.OK);

    }
    // serrve image

    @GetMapping("/image/{productid}")
    public void  serveProductImage(@PathVariable String productid, HttpServletResponse response) throws IOException {
        ProductDto byId = productService.findById(productid);

        logger.info("Image uploaded successfully : {} ",byId.getProductImageName());
        InputStream resource = fileService.getResource(imagePath, byId.getProductImageName());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());

    }
}
