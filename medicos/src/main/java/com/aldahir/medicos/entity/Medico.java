package com.aldahir.medicos.entity;

import com.aldahir.commons.enums.DisponibilidadMedico;
import com.aldahir.commons.enums.EspecialidadMedico;
import com.aldahir.commons.enums.EstadoRegistro;
import com.aldahir.commons.utils.StringCustomUtils;
import com.aldahir.commons.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MEDICOS")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MEDICO")
    private Long id;

    @Column(name = "NOMBRE", length = 50, nullable = false)
    private String nombre;

    @Column(name = "APELLIDO_PATERNO", length = 50, nullable = false)
    private String apellidoPaterno;

    @Column(name = "APELLIDO_MATERNO", length = 50, nullable = false)
    private String apellidoMaterno;

    @Column(name = "EDAD", nullable = false)
    private Short edad;

    @Column(name = "EMAIL", length = 100, nullable = false)
    private String email;

    @Column(name = "TELEFONO", length = 10, nullable = false)
    private String telefono;

    @Column(name = "CEDULA_PROFESIONAL", length = 12, nullable = false)
    private String cedulaProfesional;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESPECIALIDAD", nullable = false)
    private EspecialidadMedico especialidadMedico;

    @Enumerated(EnumType.STRING)
    @Column(name = "DISPONIBILIDAD", nullable = false)
    private DisponibilidadMedico disponibilidadMedico;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_REGISTRO", nullable = false)
    private EstadoRegistro estadoRegistro;

    public void validarDatos(String nombre, String apellidoPaterno, String apellidoMaterno, Short edad, String email, String telefono, String cedulaProfessional, EspecialidadMedico especialidad) {
        StringCustomUtils.validarTamanio(nombre, 1, 50,
                "El nombre es requerido y debe contener entre 1 y 50 caracteres");
        StringCustomUtils.validarTamanio(apellidoPaterno, 1, 50,
                "El apellido paterno es requerido y debe contener entre 1 y 50 caracteres");
        StringCustomUtils.validarTamanio(apellidoMaterno, 1, 50,
                "El apellido materno es requerido y debe contener entre 1 y 50 caracteres");
        StringCustomUtils.validarTamanio(email, 1, 100,
                "El email es requerido y debe contener entre 1 y 100 caracteres");
        StringCustomUtils.validarTamanio(telefono, 10, 10,
                "El teléfono es requerido y debe contener 10 caracteres");
        StringCustomUtils.validarTamanio(cedulaProfessional, 12, 12,
                "La cédula profesional es requerida y debe contener 12 caracteres");
        ValoresNumericosUtils.validarRangoShort(edad, (short) 18, (short) 100,
                "La edad es requerida y debe estar entre 18 y 100 años");

        if (especialidad == null)
            throw new IllegalArgumentException("La especialidad es requerida");

    }

    private void validarNoEliminado() {
        if (this.estadoRegistro == EstadoRegistro.ELIMINADO)
            throw new IllegalArgumentException("El medico ya está eliminado");
    }

    public void actualizarEspecialidad(EspecialidadMedico especialidad) {
        validarNoEliminado();

        if (especialidad == null)
            throw new IllegalArgumentException("La especialidad es requerida");

        this.especialidadMedico = especialidad;
    }

    public void actualizarDisponibilidad(DisponibilidadMedico disponibilidadMedico) {
        validarNoEliminado();

        if (disponibilidadMedico == null)
            throw new IllegalArgumentException("La disponibilidad es requerida");

        this.disponibilidadMedico = disponibilidadMedico;
    }

    public void eliminar() {
        validarNoEliminado();

        this.estadoRegistro = EstadoRegistro.ELIMINADO;
    }

    public void actualizar(String nombre, String apellidoPaterno, String apellidoMaterno, Short edad, String email, String telefono, String cedulaProfesional, EspecialidadMedico especialidadMedico) {

        validarNoEliminado();

        validarDatos(nombre, apellidoPaterno, apellidoMaterno, edad, email, telefono, cedulaProfesional, especialidadMedico);

        actualizarEspecialidad(especialidadMedico);

        this.nombre = nombre.trim();
        this.apellidoPaterno = apellidoPaterno.trim();
        this.apellidoMaterno = apellidoMaterno.trim();
        this.edad = edad;
        this.email = email.trim().toLowerCase();
        this.telefono = telefono.trim();
        this.cedulaProfesional = cedulaProfesional.trim();
    }
}
