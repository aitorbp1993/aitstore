package com.bartolome.aitor.service.impl;

import com.bartolome.aitor.dto.CartDTO;
import com.bartolome.aitor.dto.CartItemDTO;
import com.bartolome.aitor.exception.RecursoNoEncontradoException;
import com.bartolome.aitor.mapper.CartItemMapper;
import com.bartolome.aitor.model.entities.Cart;
import com.bartolome.aitor.model.entities.CartItem;
import com.bartolome.aitor.model.entities.Product;
import com.bartolome.aitor.model.entities.User;
import com.bartolome.aitor.repository.CartItemRepository;
import com.bartolome.aitor.repository.CartRepository;
import com.bartolome.aitor.repository.ProductRepository;
import com.bartolome.aitor.repository.UserRepository;
import com.bartolome.aitor.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public CartDTO obtenerCarrito(Long usuarioId) {
        Cart carrito = obtenerOCrearCarrito(usuarioId);
        List<CartItemDTO> items = carrito.getItems().stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toList());

        return new CartDTO(usuarioId, items);
    }

    @Override
    public CartDTO agregarItem(Long usuarioId, CartItemDTO itemDTO) {
        Cart carrito = obtenerOCrearCarrito(usuarioId);

        Product producto = productRepository.findById(itemDTO.getProductoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + itemDTO.getProductoId()));

        Optional<CartItem> existente = carrito.getItems().stream()
                .filter(ci -> ci.getProducto().getId().equals(producto.getId()))
                .findFirst();

        if (existente.isPresent()) {
            existente.get().setCantidad(existente.get().getCantidad() + itemDTO.getCantidad());
        } else {
            CartItem nuevoItem = new CartItem();
            nuevoItem.setCarrito(carrito);
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(itemDTO.getCantidad());
            carrito.getItems().add(nuevoItem);
        }

        cartRepository.save(carrito);
        return obtenerCarrito(usuarioId);
    }


    @Override
    public CartDTO eliminarItem(Long usuarioId, Long productoId) {
        Cart carrito = obtenerOCrearCarrito(usuarioId);
        carrito.getItems().removeIf(i -> i.getProducto().getId().equals(productoId));
        cartRepository.save(carrito);
        return obtenerCarrito(usuarioId);
    }

    @Override
    public void vaciarCarrito(Long usuarioId) {
        Cart carrito = obtenerOCrearCarrito(usuarioId);
        carrito.getItems().clear();
        cartRepository.save(carrito);
    }

    private Cart obtenerOCrearCarrito(Long usuarioId) {
        return cartRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> {
                    User usuario = userRepository.findById(usuarioId)
                            .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + usuarioId));

                    Cart nuevo = new Cart();
                    nuevo.setUsuario(usuario);
                    nuevo.setItems(new ArrayList<>());
                    return cartRepository.save(nuevo);
                });
    }

}
