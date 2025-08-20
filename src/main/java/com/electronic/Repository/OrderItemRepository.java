package com.electronic.Repository;

import com.electronic.Dto.OrderItemDto;
import com.electronic.Entity.Order_Items;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<Order_Items , Integer> {

}
