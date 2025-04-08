package com.bartolome.aitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<com.bartolome.aitor.model.entities.Order, Long> {
    List<com.bartolome.aitor.model.entities.Order> findByUsuarioId(Long usuarioId);
}
