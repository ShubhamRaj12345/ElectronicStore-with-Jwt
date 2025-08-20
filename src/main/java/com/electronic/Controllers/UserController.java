package com.electronic.Controllers;

import com.electronic.Dto.ApiResponseMessage;
import com.electronic.Dto.ImageResponse;
import com.electronic.Dto.PageableResponse;
import com.electronic.Dto.UserDto;
import com.electronic.Service.FileService;
import com.electronic.Service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/user")
@EnableMethodSecurity(prePostEnabled = true) // this is a method level security
public class  UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;

     @Value("${user.profile.image.path}")
     private  String  imageuploadpath;

     private Logger logger = LoggerFactory.getLogger(UserController.class);

  // @PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){

        UserDto user = userService.createUser(userDto);

        return  new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/update/{userId}")
    public  ResponseEntity<UserDto> updateUser(@Valid @PathVariable String userId, @RequestBody UserDto userDto){

        UserDto userDto1 = userService.updateUser(userDto, userId);

        return  new ResponseEntity<>(userDto1,HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{userId}")
    public  ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId){

        userService.deleteUser(userId);
        ApiResponseMessage userDeletedSucessfully = ApiResponseMessage.builder().message("User deleted Sucessfully ").status(HttpStatus.OK).sucess(true).build();


        return  new ResponseEntity<>(userDeletedSucessfully, HttpStatus.OK);
    }

    @GetMapping("/getall")
//    public  ResponseEntity<List<UserDto>> getAllUsers(
            public  ResponseEntity<PageableResponse<UserDto>> getAllUsers(
            @RequestParam (value = "pageNumber", defaultValue ="0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue ="10",required = false) int pageSize,
            @RequestParam(value = "sortBy" , defaultValue = "name", required = false) String  sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String  sortDir
    ){

        PageableResponse<UserDto> allUser = userService.findAllUser(pageNumber,pageSize, sortBy, sortDir);
        return  new ResponseEntity<>( allUser , HttpStatus.OK);
    }

    @GetMapping("/getbyid/{userId}")
    public  ResponseEntity<UserDto> getUserById(@PathVariable String userId){

        UserDto singleUserById = userService.getSingleUserById(userId);

        return new ResponseEntity<>(singleUserById,HttpStatus.OK);
    }

    @GetMapping("/email/{emailId}")
     public ResponseEntity<UserDto> getByEmail(@PathVariable String emailId){

       return new ResponseEntity<>(userService.getSingleUserByEmail(emailId),HttpStatus.OK);
     }

     @GetMapping("/search/{keywords}")
     public ResponseEntity<List<UserDto>>searchUser(@PathVariable String keywords){

         List<UserDto> userDtos = userService.searchUser(keywords);
         return new ResponseEntity<>(userDtos,HttpStatus.OK);
     }

     // for upload user image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@PathVariable String  userId, @RequestParam ("userImage") MultipartFile image) throws IOException {
        String s = fileService.uploadFile(image, imageuploadpath);

        UserDto singleUserById = userService.getSingleUserById(userId);
           singleUserById.setImageName(s);
        UserDto userDto = userService.updateUser(singleUserById, userId);


        ImageResponse imageResponse = ImageResponse
                .builder()
                .imageName(s)
                .message("Image uploaded successfully")
                .sucess(true)
                .status(HttpStatus.OK)
                .build();

        return ResponseEntity.ok(imageResponse);
    }
    @GetMapping("/image/{userId}")
    public void  serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
        UserDto singleUserById = userService.getSingleUserById(userId);

        logger.info("Image uploaded successfully : {} ",singleUserById.getImageName());
        InputStream resource = fileService.getResource(imageuploadpath, singleUserById.getImageName());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());

    }
}
