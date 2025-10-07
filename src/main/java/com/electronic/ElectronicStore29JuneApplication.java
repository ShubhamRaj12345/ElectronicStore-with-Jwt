package com.electronic;

import com.electronic.Entity.Role;
import com.electronic.Entity.User;
import com.electronic.Repository.RoleRepository;
import com.electronic.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@SpringBootApplication
public class ElectronicStore29JuneApplication implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(ElectronicStore29JuneApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN").orElse(null);
        if(roleAdmin==null){
            Role role1 = new Role();
            role1.setRoleID(UUID.randomUUID().toString());
            role1.setName("ROLE_ADMIN");
        roleRepository.save(role1);
        }

        Role roleUser = roleRepository.findByName("ROLE_NORMAL").orElse(null);
        if(roleUser==null){


            Role role2 = new Role();
            role2.setRoleID(UUID.randomUUID().toString());
            role2.setName("ROLE_NORMAL");
            roleRepository.save(role2);
        }

        // ek admin user bnaya
//        User user = userRepository.findByEmail("shubhamraj@gmail.com").orElse(null);
//        if(user == null){
//            user= new User();
//            user.setName("shubhamraj");
//            user.setEmail("shubhamraj@gmail.com");
//
//            user.setPassword(passwordEncoder.encode("12345"));
//            user.setRoles(Set.of(roleAdmin));
//            user.setUserId(UUID.randomUUID().toString());
//            userRepository.save(user);
//        }


    }
}
