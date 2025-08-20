package com.electronic.Service;

import com.electronic.Dto.AddItemToCartRequest;
import com.electronic.Dto.CartDto;

public interface CartService {

    // add items to cart
    // case 1 cart for user is not avalable we will create the and

    // case2 cart available add the items to cart

    CartDto addItemsToCart(String userId, AddItemToCartRequest addItemToCartRequest);

    // remove item from cart

    void removeItemFromCart(String userId, int cartItem);

    // remove all items from cart
    void clearCart(String userId);

 CartDto getCartByUser(String userid);
}

