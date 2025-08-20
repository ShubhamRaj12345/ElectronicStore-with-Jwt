package com.electronic.Repository;

import com.electronic.Entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem , Integer > {


}
