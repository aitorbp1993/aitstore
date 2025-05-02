package com.bartolome.aitor.dto;

import com.bartolome.aitor.model.enums.Rol;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO que representa a un usuario del sistema")
public class UserDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre del usuario", example = "Juan Pérez")
    private String nombre;

    @Email(message = "Debe ser un email válido")
    @NotBlank(message = "El email es obligatorio")
    @Schema(description = "Correo electrónico del usuario", example = "juan@example.com")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Schema(description = "Contraseña del usuario")
    private String password;

    @NotBlank(message = "El rol es obligatorio")
    @Schema(description = "Rol del usuario", example = "cliente")
    private Rol rol;
}
