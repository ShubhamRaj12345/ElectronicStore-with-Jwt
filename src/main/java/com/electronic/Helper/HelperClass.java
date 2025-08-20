package com.electronic.Helper;

import com.electronic.Dto.PageableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class HelperClass {

    public static < U , V>PageableResponse<V> getPageableresponse(Page <U> all, Class<V> type){

        List<U> content = all.getContent();
        // we need to return UserDto
        // yaha ek ek user milega usko collect karenge
        List<V> collect = content.stream().map(user -> new ModelMapper().map(user, type)).collect(Collectors.toList());


        PageableResponse<V> response= new PageableResponse<>();
        response.setContents(collect);
        response.setPageNumber(all.getNumber());
        response.setTotalPage(all.getTotalPages());
        response.setPageSize(all.getSize());
        response.setTotalElements(all.getTotalElements());
        response.setLastPage(all.isLast());

         return  response;
    }



}
