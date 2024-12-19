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

    @GetMapping("/addOne/{productID}")
    public String addOne(@PathVariable("productID") Long prodID, Model model){
        Cart row = cartRepository.getById(prodID);
        row.setNumberOfProducts(row.getNumberOfProducts() + 1);
        row.setTotal(row.getTotal() + row.getProduct().getPrice());
        cartRepository.save(row);
        return "redirect:/cart";
    }

    @GetMapping("/dellOne/{productID}")
    public String dellOne(@PathVariable("productID") Long prodID, Model model){
        Cart row = cartRepository.getById(prodID);
        row.setNumberOfProducts(row.getNumberOfProducts() - 1);
        row.setTotal(row.getTotal() - row.getProduct().getPrice());
        cartRepository.save(row);
        return "redirect:/cart";
    }

    @GetMapping("/addToCartOne/{productId}")
    public String addToCartOne(@PathVariable(name = "productId") Long productId, Model model) {
        cartService.addProductToCart(productId);
        return "redirect:/FrameOne";
    }


    @PostMapping("/delete/{productId}")
    public String deleteIceCream(@PathVariable Long productId) {
        cartRepository.deleteById(productId);
        return "redirect:/cart";
    }





}
