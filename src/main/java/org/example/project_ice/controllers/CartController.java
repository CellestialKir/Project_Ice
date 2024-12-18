package org.example.project_ice.controllers;


import org.example.project_ice.entity.Cart;
import org.example.project_ice.entity.Product;
import org.example.project_ice.repository.CartRepository;
import org.example.project_ice.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @GetMapping("/cart")
    public String viewCart(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        List<Cart> cart = cartService.getCartByUser(username);

        int total = 0;
        int amount = 0;

        for (Cart one_row:cart) {
            total += one_row.getTotal();
            amount += one_row.getNumberOfProducts();
        }

        model.addAttribute("cart", cart);
        model.addAttribute("fullTotal", total);
        model.addAttribute("fullAmount", amount);
        return "FrameTwo";
    }

//    public ResponseEntity<String> addToCart(@RequestBody Map<String, Long> request) {
//        Long productId = request.get("productId");
//        if (productId == null) {
//            return ResponseEntity.badRequest().body("Invalid product id");
//        }
//        cartService.addProductToCart(productId);
//        return ResponseEntity.ok("Product added to the cart");
//    }



//    @GetMapping("/addToCart/{productId}")
//    public String addToCart(@PathVariable(name = "productId") Long productId, Model model) {
//        cartService.addProductToCart(productId);
//        model.addAttribute("cart", cart);
//        return "redirect:/cart"; // Redirects to the viewCart page after adding a product
//    }

    @GetMapping("/addToCartOne/{productId}")
    public String addToCartOne(@PathVariable(name = "productId") Long productId, Model model) {
        cartService.addProductToCart(productId);
        return "redirect:/FrameOne"; // Redirects to the viewCart page after adding a product
    }


    @PostMapping("/delete/{productId}")
    public String deleteIceCream(@PathVariable Long productId) {
        cartRepository.deleteById(productId);
        return "redirect:/cart";
    }





}
