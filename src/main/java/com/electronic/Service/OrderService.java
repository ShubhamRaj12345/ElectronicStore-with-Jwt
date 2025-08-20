package com.electronic.Service;

import com.electronic.Dto.CreateOrderRequest;
import com.electronic.Dto.OrderDto;
import com.electronic.Dto.PageableResponse;

import java.util.List;

public interface OrderService {

  // create order
    OrderDto createOrder(CreateOrderRequest  orderDto );
    // remove order

    void removeOrder(String orderId);


    // get orders of user

    List<OrderDto> findOrdersByUserId(String userId);


    // get orders
    PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir);

}
