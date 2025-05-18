package com.bartolome.aitor.service.impl;

import com.bartolome.aitor.dto.CreateOrderDTO;
import com.bartolome.aitor.dto.OrderDTO;
import com.bartolome.aitor.exception.RecursoNoEncontradoException;
import com.bartolome.aitor.exception.StockInsuficienteException;
import com.bartolome.aitor.mapper.OrderMapper;
import com.bartolome.aitor.model.entities.Order;
import com.bartolome.aitor.model.entities.OrderItem;
import com.bartolome.aitor.model.entities.Product;
import com.bartolome.aitor.model.entities.User;
import com.bartolome.aitor.repository.OrderRepository;
import com.bartolome.aitor.repository.ProductRepository;
import com.bartolome.aitor.repository.UserRepository;
import com.bartolome.aitor.service.OrderService;
import com.bartolome.aitor.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final ProductService productService;

    @Override
    public List<OrderDTO> obtenerTodos() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> obtenerPorUsuario(Long usuarioId) {
        return orderRepository.findByUsuarioId(usuarioId).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderDTO> obtenerPorId(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toDto);
    }

    @Override
    @Transactional
    public OrderDTO crearPedido(CreateOrderDTO dto) {
        User usuario = userRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + dto.getUsuarioId()));

        Order pedido = new Order();
        pedido.setUsuario(usuario);
        pedido.setFechaCreacion(LocalDateTime.now());
        pedido.setEstado(dto.getEstado());

        List<OrderItem> items = dto.getItems().stream().map(itemDto -> {
            Product producto = productRepository.findById(itemDto.getProductoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + itemDto.getProductoId()));

            if (producto.getStock() < itemDto.getCantidad()) {
                throw new StockInsuficienteException("No hay suficiente stock para el producto: " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - itemDto.getCantidad());
            productRepository.save(producto); // Guardamos siempre el producto actualizado

            OrderItem item = new OrderItem();
            item.setPedido(pedido);
            item.setProducto(producto);
            item.setCantidad(itemDto.getCantidad());
            item.setPrecioUnitario(producto.getPrecio());

            return item;
        }).collect(Collectors.toList());

        pedido.setItems(items);

        double total = items.stream()
                .mapToDouble(i -> i.getPrecioUnitario() * i.getCantidad())
                .sum();
        pedido.setTotal(total);

        Order pedidoGuardado = orderRepository.save(pedido);

        // 🧹 Limpiar productos con stock 0
        productService.eliminarProductosSinStock();

        return orderMapper.toDto(pedidoGuardado);
    }

    @Override
    public void eliminarPedido(Long id) {
        orderRepository.deleteById(id);
    }
}
