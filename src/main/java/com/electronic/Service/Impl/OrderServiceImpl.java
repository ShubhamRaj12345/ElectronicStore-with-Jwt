package com.electronic.Service.Impl;

import com.electronic.Dto.CreateOrderRequest;
import com.electronic.Dto.OrderDto;
import com.electronic.Dto.PageableResponse;
import com.electronic.Entity.*;
import com.electronic.HandlingException.BadApiRequest;
import com.electronic.HandlingException.ResourceNotFoundException;
import com.electronic.Helper.HelperClass;
import com.electronic.Repository.CartRepository;
import com.electronic.Repository.OrderItemRepository;
import com.electronic.Repository.OrderRepository;
import com.electronic.Repository.UserRepository;
import com.electronic.Service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public OrderDto createOrder(CreateOrderRequest  orderDto) {

        String userId = orderDto.getUserId();
        String cartId = orderDto.getCartId();

        // fetch user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found with given id"));
        // fetch cart from cartRepository
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("cart not found with given id"));
        List<CartItem> items = cart.getItems();

        if (items.size() <=0) {
            throw new BadApiRequest("Invalid number of items");
        }
        Order order = Order.builder()
                .billingName(orderDto.getBillingName())
                .billingPhone(orderDto.getBillingPhone())
                .billingAddress(orderDto.getBillingAddress())
                .orderDate(new Date())
                .deliveryDate(null)
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .user(user)
                .build();

        AtomicReference<Integer> orderAmount = new AtomicReference<>();
        List<Order_Items> collect = items.stream().map(cartItem -> {
            Order_Items orderItems = Order_Items.builder()
                    .quantity(cartItem.getQuantity())
                    .products(cartItem.getProducts())
                    .totalPrice(cartItem.getTotalPrice() * cartItem.getProducts().getDiscountedPrice())
                    .order(order)
                    .build();

     orderAmount.set(orderAmount.get()+ orderItems.getTotalPrice());
            return orderItems;
        }).collect(Collectors.toList());

           order.setOrderItems(collect);
           order.setOrderAmount(orderAmount.get());
           cart.getItems().clear();

           cartRepository.save(cart);
        Order save = orderRepository.save(order);
        OrderDto map = modelMapper.map(save, OrderDto.class);
        return map;
    }

    @Override
    public void removeOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("order not found with given id"));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> findOrdersByUserId(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found with given id"));

        List<Order> byUser = orderRepository.findByUser(user);

        List<OrderDto> collect = byUser.stream().map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort=  (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize , sort);
        Page<Order> all = orderRepository.findAll(pageable);

        return HelperClass.getPageableresponse(all, OrderDto.class);
    }
}
