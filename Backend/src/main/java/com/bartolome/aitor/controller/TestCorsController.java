package com.bartolome.aitor.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*") // <- Permite CORS en esta ruta como prueba
public class TestCorsController {

    @GetMapping
    public String testCors() {
        return "CORS OK desde backend";
    }
}
