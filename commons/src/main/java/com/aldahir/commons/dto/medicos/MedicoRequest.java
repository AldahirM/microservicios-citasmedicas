package com.aldahir.commons.dto.medicos;

import jakarta.validation.constraints.*;

public record MedicoRequest(

        @NotBlank(message = "El nombre es requerido")
        @Size(min = 1, max = 50, message = "El nombre debe tener entre 1 y 50 caracteres")
        String nombre,

        @NotBlank(message = "El apellido paterno es requerido")
        @Size(min = 1, max = 50, message = "El nombre debe tener entre 1 y 50 caracteres")
        String apellidoPaterno,

        @NotBlank(message = "El apellido materno es requerido")
        @Size(min = 1, max = 50, message = "El nombre debe tener entre 1 y 50 caracteres")
        String apellidoMaterno,

        @NotNull(message = "El apellidoMaterno es requerido")
        @Min(value = 18, message = "La edad mínima es de 18 años")
        @Max(value = 100, message = "La edad máxima es de 18 años")
        Short edad,

        @NotBlank(message = "El email es requerido")
        @Size(min = 1, max = 100, message = "El email debe tener entre 1 y 100 caracteres")
        @Email(message = "El email debe tener el formato correcto (correo@dominio)")
        String email,

        @NotNull(message = "El email es requerido")
        @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe contener solo 10 dígitos numéricos")
        String telefono,

        @NotBlank(message = "La cedula profesional es requerida")
        @Size(min = 12, max = 12, message = "La cédula profesional debe tener 12 caracteres")
        String cedulaProfesional,

        @NotNull(message = "El id de la especialidad es requerida")
        @Positive(message = "El id de la especialidad debe ser positivo")
        Long idEspecialidad

) {
}
