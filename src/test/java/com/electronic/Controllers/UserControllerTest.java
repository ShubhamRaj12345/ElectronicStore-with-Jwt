package com.electronic.Controllers;

import com.electronic.Dto.PageableResponse;
import com.electronic.Dto.UserDto;
import com.electronic.Entity.Role;
import com.electronic.Entity.User;
import com.electronic.Service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Arrays;
import java.util.Set;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    private UserService userService;
    private Role role;
    private User user;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MockMvc mockMvc;

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
    }




    @Test
   public void createUserTest() throws Exception {

        // users+POST + user data as json
        // data as json +status created

        UserDto dto = modelMapper.map(user,UserDto.class);
        Mockito.when(userService.createUser(Mockito.any())).thenReturn(dto);

        // actual request for url
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(user))
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.name").exists());
   }


   @Test
   public void updateUserTest() throws Exception {

        String userId="123";

        UserDto dto = modelMapper.map(user,UserDto.class);
        Mockito.when(userService.updateUser(Mockito.any(),Mockito.anyString())).thenReturn(dto);

        // user/{userId} + PUT requests + json
        this.mockMvc.perform(MockMvcRequestBuilders.put("/user/update/"+userId)
                        .header(HttpHeaders.AUTHORIZATION,"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzaHViaGFtcmFqQGdtYWlsLmNvbSIsImlhdCI6MTc1OTY1NDUxMSwiZXhwIjoxNzU5NjcyNTExfQ.StwwBFAUDVnQA3dogbOXXOWpLhlqLxG-dKfX5Ln2DbF8mZrzA6Gas8QhamymuIQ6ZttL9pdFWICmUhI-ixqEAg")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonString(user))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists());

   }

// get all users
    @Test
    public void getAllUSerTest() throws Exception {

        UserDto build = UserDto.builder().name("shubham").email("shubham@gmail.com").password("shubham").about("Testing app").build();
        UserDto build1 = UserDto.builder().name("shubham").email("shubham@gmail.com").password("shubham").about("Testing app").build();
        UserDto build2 = UserDto.builder().name("shubham").email("shubham@gmail.com").password("shubham").about("Testing app").build();
        UserDto build3 = UserDto.builder().name("shubham").email("shubham@gmail.com").password("shubham").about("Testing app").build();
        UserDto build4 = UserDto.builder().name("shubham").email("shubham@gmail.com").password("shubham").about("Testing app").build();

        PageableResponse pageableResponse = new PageableResponse();
        pageableResponse.setContents(Arrays.asList(
                build,build1,build2,build3,build4
        ));
        pageableResponse.setLastPage(false);
        pageableResponse.setPageNumber(100);
        pageableResponse.setPageSize(10);
        pageableResponse.setTotalElements(1000);

        Mockito.when(userService.findAllUser(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);

        //actal call
        this.mockMvc.perform(MockMvcRequestBuilders.get("/user/getall")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
    )
        .andDo(print())
                .andExpect(status().isOk());

    }



    private String convertObjectToJsonString(Object user) throws JsonProcessingException {
        try{
            return new ObjectMapper().writeValueAsString(user);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
