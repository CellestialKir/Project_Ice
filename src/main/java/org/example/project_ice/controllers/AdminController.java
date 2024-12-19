package org.example.project_ice.controllers;
import jakarta.validation.Valid;
import org.example.project_ice.Category;
import org.example.project_ice.NotificationService;
import org.example.project_ice.entity.History;
import org.example.project_ice.entity.Product;
import org.example.project_ice.entity.User;
import org.example.project_ice.repository.*;
import org.example.project_ice.services.HistoryService;
import org.example.project_ice.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.transform.Source;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Controller()
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private HistoryService histServ;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    ProductRepository prodrepo;

    @Autowired
    ProductService prodServ;

    @GetMapping()
    public String adminDashboard(@RequestParam(required = false) Long category,
                                 @RequestParam(required = false) String status,
                                 @RequestParam(required = false) String sort,
                                 @RequestParam(value = "search", required = false) String search,
                                 Model model) {


        Sort sortOption = Sort.by("id");
        sortOption.ascending();
        model.addAttribute("ice_creams", prodrepo.findAll(sortOption));
        model.addAttribute("n_product", new Product());
        return "FrameThree_admin";
    }

    @PostMapping("/add")
    public String addTask(@Valid @ModelAttribute("product") Product product, BindingResult result,
                          @RequestParam("photo2") MultipartFile photo, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepo.findAll());
            model.addAttribute("users", userRepo.findAll());
            return "admin";
        }
        System.out.println(photo.getOriginalFilename());

        if (photo.isEmpty()) {
            System.out.println("No file uploaded.");
            prodrepo.save(product);
            return "redirect:/admin";
        }

        String uploadDir = new File("src/main/resources/templates/public/images").getAbsolutePath();
        File uploadDirectory = new File(uploadDir);

        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        try {
            String filePath = uploadDir + File.separator + photo.getOriginalFilename();
            File destinationFile = new File(filePath);
            photo.transferTo(destinationFile);

            System.out.println("File uploaded successfully: " + filePath);
            product.setImage(photo.getOriginalFilename());
            prodrepo.save(product);
        } catch (IOException e) {
            System.out.println("File upload failed: " + e.getMessage());
            try {
                String filePath = uploadDir + File.separator + "copy_" + photo.getOriginalFilename();
                File destinationFile = new File(filePath);
                photo.transferTo(destinationFile);

                System.out.println("File uploaded successfully: " + filePath);
                product.setImage(photo.getOriginalFilename());
                prodrepo.save(product);
            }catch (IOException ex){
                System.out.println("File upload failed: " + e.getMessage());
            }
        }
        return "redirect:/admin";
    }

    @PostMapping("/edit")
    public String editTask(@Valid @ModelAttribute("product") Product prod,
                           BindingResult result, @RequestParam("photo") MultipartFile photo, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepo.findAll());
            model.addAttribute("users", userRepo.findAll());
            return "admin";
        }
        Optional<Product> n_prod = prodrepo.findById(prod.getId());
        if (n_prod.isPresent()) {
            Product new_product = n_prod.get();
            new_product.setId(prod.getId());
            new_product.setDescription(prod.getDescription());
            new_product.setName(prod.getName());
            new_product.setPrice(prod.getPrice());
            new_product.setAvailableAmount(prod.getAvailableAmount());

            if (photo.isEmpty()) {
                System.out.println("No file uploaded.");
                prodrepo.save(new_product);
                return "redirect:/admin";
            }

            String uploadDir = new File("src/main/resources/templates/public/images").getAbsolutePath();
            File uploadDirectory = new File(uploadDir);

            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdirs();
            }

            try {
                String filePath = uploadDir + File.separator + photo.getOriginalFilename();
                File destinationFile = new File(filePath);
                photo.transferTo(destinationFile);

                System.out.println("File uploaded successfully: " + filePath);
                new_product.setImage(photo.getOriginalFilename());
                prodrepo.save(new_product);
            } catch (IOException e) {
                System.out.println("File upload failed: " + e.getMessage());
                try {
                    String filePath = uploadDir + File.separator + "copy_" + photo.getOriginalFilename();
                    File destinationFile = new File(filePath);
                    photo.transferTo(destinationFile);

                    System.out.println("File uploaded successfully: " + filePath);
                    new_product.setImage(photo.getOriginalFilename());
                    prodrepo.save(new_product);
                } catch (IOException ex) {
                    System.out.println("File upload failed: " + e.getMessage());
                }
            }
        }

        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable("id") Long prodId) {
        Optional<Product> prod = prodrepo.findById(prodId);
        if (prod.isPresent()) {
            prodrepo.delete(prod.get());
        }
        return "redirect:/admin";
    }


    @GetMapping("/increment/{id}")
    public String increment(@PathVariable("id") Long prodId){
        Optional<Product> prod = prodrepo.findById(prodId);
        if (prod.isPresent()) {
            Product product = prod.get();
            product.setAvailableAmount(product.getAvailableAmount() + 1);
            prodrepo.save(product);
        }
        return "redirect:/admin";
    }

    @GetMapping("/decrement/{id}")
    public String decrement(@PathVariable("id") Long prodId){
        Optional<Product> prod = prodrepo.findById(prodId);
        if (prod.isPresent()) {
            Product product = prod.get();
            product.setAvailableAmount(product.getAvailableAmount() - 1);
            prodrepo.save(product);
        }
        return "redirect:/admin";
    }

    @GetMapping("/history")
    public String history(@RequestParam(value = "search", required = false) String search, Model model) throws ParseException {
        List<History> histories;
        System.out.println(search);
        if (search != null && !search.isEmpty()) {
            System.out.println(search);
            histories = histServ.findBySearch(search);
        }
        else {
            histories = histServ.findAll();
        }
        model.addAttribute("history_rows", histories);
        return "tableFrame";
    }
}
