package com.bartolome.aitor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta de autenticación que incluye el token JWT")
public class AuthResponse {
    private String token;
    private Long usuarioId;
    private String nombre;
    private String refreshToken;

}
