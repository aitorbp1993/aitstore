package com.bartolome.aitor.repository;

import com.bartolome.aitor.model.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
