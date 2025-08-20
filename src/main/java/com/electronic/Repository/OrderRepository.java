package com.electronic.Repository;

import com.electronic.Entity.Order;
import com.electronic.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,String> {

    List<Order> findByUser(User user);
}
