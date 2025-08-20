package com.electronic.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_items")
@Data
@Builder
public class Order_Items {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderItemId;
    private  int quantity;
    private int totalPrice;
    @OneToOne
    @JoinColumn(name="product_id")
    private Products products;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
