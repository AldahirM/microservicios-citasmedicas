package com.aldahir.commons.dto.pacientes;

import jakarta.validation.constraints.*;

public record PacienteRequest(
        @NotNull(message = "El nombre no debe ser vacío")
        @Size(min = 1, max = 50, message = "El tamaño del nombre debe estar entre 1 y 50 caracteres")
        String nombre,
        @NotNull(message = "El apellido paterno no debe ser vacío")
        @Size(min = 1, max = 50, message = "El tamaño del apellido paterno debe estar entre 1 y 50 caracteres")
        String apellidoPaterno,
        @NotNull(message = "El apellido materno no debe ser vacío")
        @Size(min = 1, max = 50, message = "El tamaño del apellido materno debe estar entre 1 y 50 caracteres")
        String apellidoMaterno,
        @NotNull(message = "La edad no debe ser vacía")
        @Min(value = 1, message = "la edad debe ser positiva")
        @Max(value = 100, message = "La edad no puede ser mayor a 100")
        Short edad,
        @NotNull(message = "La estatura no debe ser vacío")
        @DecimalMin(value = "0.1", message = "La estatura debe superar el metro")
        @DecimalMax(value = "200", message = "La estatura debe superar el metro")
        Double peso,
        @NotNull(message = "La estatura no debe ser vacío")
        @DecimalMin(value = "1.0", message = "La estatura debe superar el metro")
        @DecimalMax(value = "2.0", message = "La estatura debe superar el metro")
        Double estatura,
        @NotNull(message = "El email no debe ser vacío")
        @Size(min = 1, max = 100, message = "El email debe tener entre 1 y 100 caracteres")
        @Email(message = "El email no es válido")
        String email,
        @NotNull(message = "El teléfono no debe ser vacío")
        @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe contener solo 10 dígitos numéricos")
        @Size(min = 10, max = 15, message = "El teléfono debe tener entre 10 y 15 caracteres")
        String telefono,
        @NotNull(message = "La dirección no debe ser vacío")
        @Size(min = 1, max = 150, message = "La dirección debe tener entre 10 y 150 caracteres")
        String direccion
) {
}
