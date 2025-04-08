package com.bartolome.aitor.repository;

import com.bartolome.aitor.model.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
