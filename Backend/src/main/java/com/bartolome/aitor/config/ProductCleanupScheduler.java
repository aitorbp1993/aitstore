package com.bartolome.aitor.config;

import com.bartolome.aitor.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductCleanupScheduler {

    private final ProductService productService;

    // Ejecuta cada día a las 2:00 AM
    @Scheduled(cron = "0 0 2 * * *")
    public void eliminarProductosSinStock() {
        log.info("⏰ Iniciando limpieza automática de productos sin stock...");
        productService.eliminarProductosSinStock();
        log.info("✅ Productos sin stock eliminados correctamente.");
    }
}