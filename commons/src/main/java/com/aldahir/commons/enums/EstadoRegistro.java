package com.aldahir.commons.enums;


import com.aldahir.commons.exceptions.RecursoNoEncontradoException;
import com.aldahir.commons.utils.StringCustomUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EstadoRegistro {
    ACTIVO("Activo"),
    ELIMINADO("Eliminado");

    private final String descripcion;

    public static EstadoRegistro obtenerEstadoRegistroPorDescripcion(String descripcion) {
        StringCustomUtils.validarNoVacio(descripcion, "La descripción es requerida");

        String descripcionNormalizada = StringCustomUtils.normalizarTexto(descripcion);

        for (EstadoRegistro estadoRegistro : values()) {
            if (StringCustomUtils.quitarAcentos(descripcionNormalizada).equals(StringCustomUtils.quitarAcentos(estadoRegistro.getDescripcion())))
                return estadoRegistro;
        }
        throw new RecursoNoEncontradoException("No existe el estado de registro con ese descripción");
    }
}
