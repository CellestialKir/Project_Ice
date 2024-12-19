package org.example.project_ice.services;

import lombok.Getter;
import org.example.project_ice.entity.Cart;
import org.example.project_ice.entity.Product;
import org.example.project_ice.entity.User;
import org.example.project_ice.repository.CartRepository;
import org.example.project_ice.repository.ProductRepository;
import org.example.project_ice.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepo userRepository;

    public Cart getCartByUserId(Long userId) {
        return cartRepository.findById(userId).orElse(null);
    }

    public List<Cart> getCartByUser(String username) {
        return cartRepository.findAllByUser_Username(username);
    }

    private Cart createCartForUser(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        // Create a new Cart
        Cart newCart = new Cart();
        newCart.setUser(user);
        newCart.setProduct(new Product());

        // Save the new cart to the database
        return cartRepository.save(newCart);
    }

    public void addProductToCart(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (!optionalProduct.isPresent()) {
            throw new IllegalArgumentException("Product not found with id: " + productId);
        }

        Product product = optionalProduct.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Current user is not authenticated.");
        }

        String username;
        if (authentication.getPrincipal() instanceof UserDetails) {
            username = ((UserDetails) authentication.getPrincipal()).getUsername();
        } else {
            username = authentication.getPrincipal().toString();
        }

        User optionalUser = userRepository.findByUsername(username);

        Cart cart = new Cart();

        cart.setProduct(product);
        cart.setUser(optionalUser);
        cart.setTotal(product.getPrice());
        cart.setNumberOfProducts(1);
        cartRepository.save(cart);
    }

    public void delete(Cart cart){
        cartRepository.delete(cart);
    }

}
