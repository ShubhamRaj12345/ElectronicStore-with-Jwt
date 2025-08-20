package com.electronic.Dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CategoryDto {

    private  String categoryId;
    @Size(min = 3,message = "title must be minimum 3 ")
    @NotBlank(message = "title is required")
    private String title;
    @NotBlank
    private  String description;
    private  String coverImage;

}
