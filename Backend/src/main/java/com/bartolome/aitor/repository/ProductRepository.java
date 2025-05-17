package com.bartolome.aitor.repository;

import com.bartolome.aitor.model.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNombreContainingIgnoreCase(String nombre);

}
