package org.example.project_ice.controllers;

import org.example.project_ice.entity.Cart;
import org.example.project_ice.entity.Product;
import org.example.project_ice.entity.User;
import org.example.project_ice.repository.ProductRepository;
import org.example.project_ice.repository.UserRepo;
import org.example.project_ice.services.CartService;
import org.example.project_ice.services.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HistoryController {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private CartService cartService;

    @GetMapping("/addToHistory")
    public String addToHistory(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        List<Cart> rows = cartService.getCartByUser(username);
        for(Cart row: rows){
            Long productId = row.getProduct().getId();
            Product product = productRepo.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            User user = userRepo.findUserByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));


            historyService.addToHistory(user, product, row.getNumberOfProducts());
            cartService.delete(row);

        }
        return "redirect:/cart";
    }
}
