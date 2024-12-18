package org.example.project_ice.controllers;
import org.example.project_ice.entity.User;
import org.example.project_ice.repository.ProductRepository;
import org.example.project_ice.repository.UserRepo;
import org.example.project_ice.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProductRepository prodrepo;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }


    @GetMapping("/FrameOne")
    public String frameOne(Model model) {

        Sort sortOption = Sort.by("availableAmount");
        sortOption.ascending();

        model.addAttribute("products", prodrepo.findAll(sortOption));
        return "FrameOne";
    }

}