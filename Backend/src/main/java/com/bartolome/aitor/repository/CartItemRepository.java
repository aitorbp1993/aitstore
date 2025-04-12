package com.bartolome.aitor.repository;

import com.bartolome.aitor.model.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}

