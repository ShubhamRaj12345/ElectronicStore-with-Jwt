package com.electronic.Service;

import com.electronic.Dto.PageableResponse;
import com.electronic.Dto.UserDto;
import com.electronic.Entity.Role;
import com.electronic.Entity.User;
import com.electronic.Repository.RoleRepository;
import com.electronic.Repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

// this is a junit apply
@SpringBootTest
//@ExtendWith(MockitoExtension.class) //jis class me mockito use karna hia us me ye annotaion use karte hai
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private UserService userService;

    User user;

    Role role;

    String roleId;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    public  void init(){
        role = Role.builder().roleID("abc").name("NORMAL").build();
        user = User.builder()
                .name("shubham")
                .email("shubhamraj@gmail.com")
                .about("about")
                .password("password")
                .gender("Male")
                .roles(Set.of(role))
                .imageName("avi.png")
                .build();

        roleId="abc";

    }
    // create user
    @Test
    public void createUserTest(){

        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        Mockito.when(roleRepository.findById(Mockito.anyString())).thenReturn(Optional.of(role));

        UserDto user1 = userService.createUser(modelMapper.map(user, UserDto.class));
        System.out.println(user1.getName());

        Assertions.assertNotNull(user1);
        Assertions.assertEquals("shubham",user1.getName());
    }

    // testing update method
//    @Test
//    public void  updateUserTest(){
//        String userId = "gjagfldghfsgd";
//        UserDto userDto = UserDto.builder()
//                .name("shubham raj")
//                .about("about this is updated user")
//                .password(passwordEncoder.encode("password"))
//                .gender("Male")
//                .imageName("avi.png")
//                .build();
//        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
//        Mockito.when(roleRepository.save(Mockito.any())).thenReturn(user);
//
//        UserDto userDto1 = userService.updateUser(userDto, userId);
//        System.out.println(userDto1.getName());
//        Assertions.assertNotNull(userDto1);
//    }





    @Test
    public void updateUserTest() {
        String userId = "gjagfldghfsgd";

        // Create a mock user entity to simulate database data
        User user = User.builder()
                .userId(userId)
                .name("Old Name")
                .about("Old about")
                .password("oldpassword")
                .gender("Male")
                .imageName("old.png")
                .build();

        UserDto userDto = UserDto.builder()
                .name("shubham raj")
                .about("about this is updated user")
                .password(passwordEncoder.encode("password"))
                .gender("Male")
                .imageName("avi.png")
                .build();

        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));

        UserDto userDto1 = userService.updateUser(userDto, userId);

        Assertions.assertNotNull(userDto1);
        Assertions.assertEquals("shubham raj", userDto1.getName());
        Assertions.assertEquals("about this is updated user", userDto1.getAbout());
    }

    // for delete user test
    @Test
    public void deleteUserTest(){
        String userId= "jsahdasgjdh";
        Mockito.when(userRepository.findById("jsahdasgjdh")).thenReturn(Optional.of(user));

        userService.deleteUser(userId);
        Mockito.verify(userRepository, Mockito.times(1)).delete(user);

    }

    @Test
    public void getAllUsersTest(){

        User user1 = User.builder()
                .name("Old Name")
                .about("Old about")
                .password("oldpassword")
                .gender("Male")
                .imageName("old.png")
                .build();

        User user2 = User.builder()
                .name("shubhamraj")
                .about("Old about")
                .password("oldpassdfsdsword")
                .gender("Male")
                .imageName("old.png")
                .build();


        List<User> userList= Arrays.asList(user, user1, user2);
        Page<User> page = new PageImpl<>(userList);
        Mockito.when(userRepository.findAll((Pageable) Mockito.any())).thenReturn(page);

//        Sort sort = Sort.by(Sort.Direction.ASC, "name");
//
//        Pageable pageable = PageRequest.of(1, 2,sort);
       PageableResponse<UserDto> allUser=  userService.findAllUser(1,2,"name","asc");

       Assertions.assertEquals(3 ,allUser.getTotalElements());
    }

    @Test
    public void getSingleUserTest(){

        String userId = "gjagfldghfsgd";
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //actual call of service method
        UserDto singleUserById = userService.getSingleUserById(userId);
        Assertions.assertNotNull(singleUserById);
        Assertions.assertEquals(user.getName(),singleUserById.getName());
    }


    @Test
    public void getSingleUserByEmailTest(){

        String emailid="shubhamraj@gmail.com";
        Mockito.when(userRepository.findByEmail(emailid)).thenReturn(Optional.of(user));
        UserDto singleUserById = userService.getSingleUserByEmail(emailid);
        Assertions.assertNotNull(singleUserById);
        Assertions.assertEquals(user.getName(),singleUserById.getName() ,"Email not matched");
    }

    @Test
    public void searchUserTest()
    {
        User user3 = User.builder()
                .name("raj")
                .about("Old about")
                .password("oldpassword")
                .gender("Male")
                .imageName("old.png")
                .build();

        User user4 = User.builder()
                .name("shubhamraj")
                .about("Old about")
                .password("oldpassdfsdsword")
                .gender("Male")
                .imageName("old.png")
                .build();


        User user5 = User.builder()
                .name("ankit")
                .about("Old about")
                .password("oldpassdfsdsword")
                .gender("Male")
                .imageName("old.png")
                .build();

        String keywords="kumar";
        Mockito.when(userRepository.findByNameContaining(keywords)).thenReturn(Arrays.asList(user3,user4,user5));


        // actaul call
        List<UserDto> userDtos = userService.searchUser(keywords);
        Assertions.assertNotNull(userDtos);
        Assertions.assertEquals(3,userDtos.size() ,"size not match ");
    }

}
