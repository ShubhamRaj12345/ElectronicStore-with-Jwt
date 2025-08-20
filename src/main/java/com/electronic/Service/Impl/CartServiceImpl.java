package com.electronic.Service.Impl;

import com.electronic.Dto.AddItemToCartRequest;
import com.electronic.Dto.CartDto;
import com.electronic.Entity.Cart;
import com.electronic.Entity.CartItem;
import com.electronic.Entity.Products;
import com.electronic.Entity.User;
import com.electronic.HandlingException.BadApiRequest;
import com.electronic.HandlingException.ResourceNotFoundException;
import com.electronic.Repository.CartItemRepository;
import com.electronic.Repository.CartRepository;
import com.electronic.Repository.ProductRepository;
import com.electronic.Repository.UserRepository;
import com.electronic.Service.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService
{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CartItemRepository cartItemRepository;


    @Override
    public CartDto addItemsToCart(String userId, AddItemToCartRequest addItemToCartRequest) {

       int quality = addItemToCartRequest.getQuantity();
       String  productId = addItemToCartRequest.getProductId();

       if (quality <= 0)
       {
           throw  new BadApiRequest("Requested quantity is not valid ");
       }
       Products products = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with given id"));

        // fetch the user from db
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id"));

        Cart cart= null;
        try{
            cartRepository.findByUser(user).get();

        }
        catch (NoSuchElementException ex)
        {
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }
        // if cartitems already present  then update
        // perform cart operation

//        boolean update = false;
        AtomicReference<Boolean> update = new AtomicReference<>(false);
        List<CartItem> items = cart.getItems();

        items= items.stream().map(item -> {
            if (item.getProducts().getProductid().equals(productId)) {
                item.setQuantity(quality);
                item.setTotalPrice(quality * products.getDiscountedPrice());
                update .set(true);
            }
            return item;
        }).collect(Collectors.toList());

//      cart.setItems(updateditems);

        // create items
     if (!update.get())
     {
         CartItem cartItem =  CartItem .builder()
                 .quantity(quality)
                 .totalPrice(quality * products .getDiscountedPrice())
                 .cart(cart)
                 .products(products)
                 .build();
         cart.getItems().add(cartItem);
         cart.setUser(user);

     }
        Cart updatedcart = cartRepository.save(cart);

        return modelMapper.map(updatedcart,CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItem) {

        // conditions
        CartItem cartItem1 = cartItemRepository.findById(cartItem).orElseThrow(() -> new ResourceNotFoundException("Item not found with given id"));
        cartItemRepository.delete(cartItem1);
    }

    @Override
    public void clearCart(String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("cart of  given  user is not found "));

        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userid) {

        User user = userRepository.findById(userid).orElseThrow(() -> new ResourceNotFoundException("User not found with given id"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("cart of  given  user is not found "));

        return modelMapper.map(cart, CartDto.class);
    }
}
