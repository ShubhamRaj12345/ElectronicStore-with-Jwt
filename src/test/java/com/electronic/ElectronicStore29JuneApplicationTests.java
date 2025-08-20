package com.electronic;

import com.electronic.Entity.User;
import com.electronic.Repository.UserRepository;
import com.electronic.Security.JwtHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ElectronicStore29JuneApplicationTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtHelper jwtHelper;



    @Test
    void contextLoads() {

        System.out.println("testing our project ");
    }

    @Test
    void testToken(){
        System.out.println("TestToken");

          String  name = "shubham";

        User user = userRepository.findByEmail("shubhamraj@gmail.com").get();
        String issuerFromToken = jwtHelper.getIssuerFromToken(user);

        System.out.println(issuerFromToken);

        System.out.println("Getting username from token ");
        System.out.println(jwtHelper.getUsernameFromToken(issuerFromToken));

        System.out.println(jwtHelper.isTokenExpired(issuerFromToken));
    }




}
