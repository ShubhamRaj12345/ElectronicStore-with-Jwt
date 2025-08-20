package com.electronic.Dto;

import com.electronic.Entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {

    private  String cartId;
    private Date createdAt;

    private List<CartItemsDto> items = new ArrayList<>();
}
