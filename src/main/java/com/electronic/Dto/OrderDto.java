package com.electronic.Dto;

import com.electronic.Entity.Order_Items;
import com.electronic.Entity.User;
import jakarta.persistence.*;
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
public class OrderDto {

    private String orderId;
    private  String orderStatus ="PENDING";
    private  String paymentStatus ="NOTPAID";
    private int orderAmount;
    private String billingAddress;
    private String billingPhone;
    private String billingName;
    private Date orderDate = new Date();
    private Date deliveryDate;
  //  private UserDto user;
    private List<OrderDto> orderItems = new ArrayList<>();
}
