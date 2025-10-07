package com.electronic.Dto;

import com.electronic.Entity.Role;
import com.electronic.Validate.ImageNameValid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String  userId;
    @NotBlank(message = "Enter name")
    @Size(min = 1, max = 20,message = "Invalid name")
    private String  name;

    @Email(message="invalid user email")
    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",message ="Invalid user email")
    private String email;

    @NotBlank(message = "password is required")
//    @Min(value = 5,message ="password is minimum 5 required")
//    @Max(value = 10, message = "maximum password is 10 ")
    private String password;

    @Size(min = 4, max = 6,message = "Invalid gender")
    private String gender;

    @NotBlank(message ="write something about yourself")
    private  String about;

    @ImageNameValid
    private String imageName;

    private Set<RoleDto> roles = new HashSet<>();
}
