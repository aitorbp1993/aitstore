package com.bartolome.aitor.repository;

import com.bartolome.aitor.model.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUsuarioId(Long usuarioId);
}
