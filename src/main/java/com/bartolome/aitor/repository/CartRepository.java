package com.bartolome.aitor.repository;

import com.bartolome.aitor.model.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUsuarioId(Long usuarioId);
}
