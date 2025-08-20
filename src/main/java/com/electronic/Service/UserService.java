package com.electronic.Service;

import com.electronic.Dto.PageableResponse;
import com.electronic.Dto.UserDto;
import org.springframework.stereotype.Service;
import java.util.List;


public interface UserService {

//    User createUser(User user);
//
//    User updateUser(User user , String userId);
//
//    User gatAllUser(User userid);

    UserDto  createUser(UserDto userDto);
    UserDto updateUser(UserDto userDto , String  userId);

    void deleteUser(String  userId);

//    List<UserDto> findAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);
PageableResponse<UserDto> findAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);



    UserDto getSingleUserById(String  userId);

    UserDto getSingleUserByEmail(String email);

    // search keyboard
    List<UserDto> searchUser(String  keyword);
}
