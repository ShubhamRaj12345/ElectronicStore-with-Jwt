package com.electronic.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemsDto {


    private int cartItemsId;

    private ProductDto products;

    private int quantity;

    private  int totalPrice;

}
