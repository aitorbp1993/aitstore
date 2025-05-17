package com.bartolome.aitor.service;

import com.bartolome.aitor.dto.CategoriaConProductosDTO;

import java.util.List;

public interface HomeService {
    List<CategoriaConProductosDTO> obtenerCategoriasConProductos();

    List<CategoriaConProductosDTO> buscarPorNombre(String nombre);
}