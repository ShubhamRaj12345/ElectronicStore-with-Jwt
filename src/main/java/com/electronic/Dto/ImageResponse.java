package com.electronic.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageResponse {

        private  String imageName;
        private String message;
        private boolean sucess;
        private HttpStatus status;
    }


