package com.electronic.Dto;

import com.electronic.Entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemDto {

    private int orderItemId;
    private  int quantity;
    private int totalPrice;
    private ProductDto products;
    private Order order;
}
