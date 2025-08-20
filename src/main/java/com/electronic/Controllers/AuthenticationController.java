package com.electronic.Controllers;

import com.electronic.Dto.JwtResponse;
import com.electronic.Dto.JwttRequest;
import com.electronic.Dto.UserDto;
import com.electronic.Entity.User;
import com.electronic.Security.JwtHelper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService  userDetailsService;

    Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/generate-token")
    private ResponseEntity<JwtResponse> login(@RequestBody JwttRequest jwttRequest) {

        logger.info("Username {} Password {} : " ,jwttRequest.getEmail() ,jwttRequest.getPassword());

        // is username and password ko authenticate karne ke liye AuthenticationManager hota bo authenticate karta hai
     this.doauthenticate(jwttRequest.getEmail(),jwttRequest.getPassword());
         // generate token
        // send karna hai response me
        User user =(User) userDetailsService.loadUserByUsername(jwttRequest.getEmail());
        String token = jwtHelper.generateToken(user);
        JwtResponse build = JwtResponse.builder().token(token).user(modelMapper.map(user, UserDto.class)).build();


        return ResponseEntity.ok(build);
    }

    private void doauthenticate(String email, String password) {

        try{
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email, password);
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        }catch (BadCredentialsException ex){
            throw  new BadCredentialsException("Invalid  Username and Password");
        }
    }


}
