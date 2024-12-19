package org.example.project_ice.repository;

import com.fasterxml.jackson.datatype.jsr310.deser.key.LocalDateKeyDeserializer;
import org.example.project_ice.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HistoryREpository extends JpaRepository<History,Long> {
    List<History> findByDate(LocalDate date);
    List<History> findByNumberOfProducts(int number);
    List<History> findByTotal(int number);
    List<History> findByUser_UsernameContaining(String username);
    List<History> findByProduct_NameContaining(String name);
    List<History> findAllByOrderByDateDesc();
    List<History> findAllByUser_Username(String username);}
