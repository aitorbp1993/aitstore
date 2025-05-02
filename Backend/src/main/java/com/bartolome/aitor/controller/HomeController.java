package com.bartolome.aitor.controller;

import com.bartolome.aitor.dto.CategoriaConProductosDTO;
import com.bartolome.aitor.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/categorias-productos")
    public ResponseEntity<List<CategoriaConProductosDTO>> obtenerCategoriasConProductos() {
        return ResponseEntity.ok(homeService.obtenerCategoriasConProductos());
    }
}
