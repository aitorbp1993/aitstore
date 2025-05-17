package com.bartolome.aitor.controller;

import com.bartolome.aitor.dto.CategoriaConProductosDTO;
import com.bartolome.aitor.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/categorias-productos")
    public ResponseEntity<List<CategoriaConProductosDTO>> obtenerCategoriasConProductos(
            @RequestParam(required = false) String search
    ) {
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(homeService.buscarPorNombre(search));
        }
        return ResponseEntity.ok(homeService.obtenerCategoriasConProductos());
    }

}
