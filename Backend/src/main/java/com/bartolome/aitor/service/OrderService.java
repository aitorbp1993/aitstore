package com.bartolome.aitor.service;

import com.bartolome.aitor.dto.CreateOrderDTO;
import com.bartolome.aitor.dto.OrderDTO;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    List<OrderDTO> obtenerTodos();

    List<OrderDTO> obtenerPorUsuario(Long usuarioId);

    Optional<OrderDTO> obtenerPorId(Long id);

    OrderDTO crearPedido(CreateOrderDTO dto);

    void eliminarPedido(Long id);
}
