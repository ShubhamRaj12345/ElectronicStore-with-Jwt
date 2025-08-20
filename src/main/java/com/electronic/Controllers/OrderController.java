package com.electronic.Controllers;

import com.electronic.Dto.ApiResponseMessage;
import com.electronic.Dto.CreateOrderRequest;
import com.electronic.Dto.OrderDto;
import com.electronic.Dto.PageableResponse;
import com.electronic.Service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PreAuthorize("hasRole('NORMAL')")
    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(@Valid  @RequestBody CreateOrderRequest request) {

        OrderDto order = orderService.createOrder(request);
        return  new ResponseEntity<>(order, HttpStatus.OK);

    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId) {

         orderService.removeOrder(orderId);

        ApiResponseMessage orderHasBeenDeletedSuccessfully = ApiResponseMessage.builder()
                .message("Order has been deleted successfully")
                .status(HttpStatus.OK)
                .sucess(true)
                .build();
        return new ResponseEntity<>(orderHasBeenDeletedSuccessfully , HttpStatus.OK);

    }

    // get orders of the user
    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    @GetMapping("/user/{userId}")
    public  ResponseEntity<List<OrderDto>>  getOrderOfUser(@PathVariable String userId) {
        List<OrderDto> ordersByUserId = orderService.findOrdersByUserId(userId);
        return  new ResponseEntity<>(ordersByUserId , HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getall")
    public  ResponseEntity<PageableResponse<OrderDto>> getAllOrders(
            @RequestParam (value = "pageNumber" ,defaultValue = "0",required = false) int pageNumber,
            @RequestParam (value = "pageSize" , defaultValue = "10" , required = false) int pageSize,
            @RequestParam(value = "sortBy" ,defaultValue = "title", required = false ) String  sortBy,
            @RequestParam(value = "sortdir" , defaultValue = "asc" , required = false ) String  sortDir
    ){
        PageableResponse<OrderDto> allOrders = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir);
        return  new ResponseEntity<>(allOrders, HttpStatus.OK);
    }
}
