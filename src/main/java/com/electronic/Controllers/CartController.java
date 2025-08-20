package com.electronic.Controllers;

import com.electronic.Dto.AddItemToCartRequest;
import com.electronic.Dto.ApiResponseMessage;
import com.electronic.Dto.CartDto;
import com.electronic.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {
    @Autowired
    private CartService cartService;

    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId,@RequestBody AddItemToCartRequest addItemToCartRequest) {
        CartDto cartDto = cartService.addItemsToCart(userId, addItemToCartRequest);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);

    }
    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable String userId, @PathVariable int itemId) {

        cartService.removeItemFromCart(userId, itemId);
        ApiResponseMessage itemRemovedSuccessfully = ApiResponseMessage.builder()
                .message("item removed successfully")
                .sucess(true)
                .status(HttpStatus.OK).
                build();

        return new ResponseEntity<>(itemRemovedSuccessfully,HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId) {

        cartService.clearCart(userId);
        ApiResponseMessage itemRemovedSuccessfully = ApiResponseMessage.builder()
                .message("now cart blanked successfully")
                .sucess(true)
                .status(HttpStatus.OK).
                build();

        return new ResponseEntity<>(itemRemovedSuccessfully,HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(@RequestBody String userId) {
        CartDto cartDto = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);

    }
}
