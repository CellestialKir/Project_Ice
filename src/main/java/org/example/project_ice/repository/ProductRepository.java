package org.example.project_ice.repository;

import org.example.project_ice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findAll(Sort sort);

    @Query("SELECT p.id FROM Product p")
    List<Long> findAllId();

    Optional<Product> findById(Long id);
}
