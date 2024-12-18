package org.example.project_ice.repository;

import org.example.project_ice.entity.Cart;
import org.example.project_ice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByUser(User user);
    List<Cart> findAllByUser_Username(String username);
}
