package org.example.project_ice.entity;

import jakarta.persistence.*;


import java.util.List;

@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int total;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private int numberOfProducts;

    public Cart() {
    }

    public Cart(Long id, User user, int total, List<Product> products, int numberOfProducts) {
        this.id = id;
        this.user = user;
        this.total = total;
        this.numberOfProducts = numberOfProducts;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getNumberOfProducts() {
        return numberOfProducts;
    }

    public void setNumberOfProducts(int numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
    }


}