package com.bartolome.aitor.exception.handler;

import com.bartolome.aitor.exception.*;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ApiError> manejarNoEncontrado(RecursoNoEncontradoException ex, WebRequest request) {
        ApiError error = construirError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getDescription(false), null);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OperacionNoPermitidaException.class)
    public ResponseEntity<ApiError> manejarOperacionNoPermitida(OperacionNoPermitidaException ex, WebRequest request) {
        ApiError error = construirError(HttpStatus.FORBIDDEN, ex.getMessage(), request.getDescription(false), null);
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> manejarValidacion(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> detalles = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.toList());

        ApiError error = construirError(HttpStatus.BAD_REQUEST, "Errores de validación", request.getDescription(false), detalles);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> manejarGenerico(Exception ex, WebRequest request) {
        ApiError error = construirError(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado", request.getDescription(false), List.of(ex.getMessage()));
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ApiError construirError(HttpStatus status, String mensaje, String path, List<String> detalles) {
        return ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(mensaje)
                .path(path.replace("uri=", ""))
                .detalles(detalles)
                .build();
    }
}
