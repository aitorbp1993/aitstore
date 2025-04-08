package com.bartolome.aitor.service;

import com.bartolome.aitor.dto.CartDTO;
import com.bartolome.aitor.dto.CartItemDTO;

public interface CartService {

    CartDTO obtenerCarrito(Long usuarioId);

    CartDTO agregarItem(Long usuarioId, CartItemDTO itemDTO);

    CartDTO eliminarItem(Long usuarioId, Long productoId);

    void vaciarCarrito(Long usuarioId);
}
