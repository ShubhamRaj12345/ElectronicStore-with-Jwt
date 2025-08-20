package com.electronic.HandlingException;

import com.electronic.Dto.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.View;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final View error;
    // handle resource notfound exception
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler(View error) {
        this.error = error;
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundException(ResourceNotFoundException e){

        logger.info("Exception Handler is invoked !!");

        ApiResponseMessage resourceNotFound = ApiResponseMessage.builder()
                .message(e.getMessage())
                .sucess(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(resourceNotFound,HttpStatus.OK);
    }

    // for method argument notValid Exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidExeception(MethodArgumentNotValidException ex){

        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();

        Map<String , Object> response = new HashMap<>();
        allErrors.stream().forEach(error -> {
            String defaultMessage = error.getDefaultMessage();

            String field = ((FieldError) error).getField();

            response.put(field,defaultMessage);
        });

        return  new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(BadApiRequest.class)
    public ResponseEntity<ApiResponseMessage> badApiRequest(BadApiRequest e){

        logger.info("Exception Handler is invoked !!");

        ApiResponseMessage resourceNotFound = ApiResponseMessage.builder()
                .message(e.getMessage())
                .sucess(false)
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(resourceNotFound,HttpStatus.BAD_REQUEST);
    }
}
