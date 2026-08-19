package com.aldahir.citas.entity;

import com.aldahir.citas.enums.EstadoCita;
import com.aldahir.commons.enums.EstadoRegistro;
import com.aldahir.commons.utils.StringCustomUtils;
import com.aldahir.commons.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "CITAS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CITA")
    private Long id;

    @Column(name = "ID_PACIENTE", nullable = false)
    private Long idPaciente;

    @Column(name = "ID_MEDICO", nullable = false)
    private Long idMedico;

    @Column(name = "FECHA_CITA", nullable = false)
    private LocalDateTime fechaCita;

    @Column(name = "SINTOMAS", nullable = false, length = 500)
    private String sintomas;

    @Column(name = "ESTADO_CITA", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoCita estadoCita;

    @Column(name = "ESTADO_REGISTRO", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoRegistro estadoRegistro;

    private static void validarId(Long id, String campo) {
        ValoresNumericosUtils.validarLongPositivo(id, "El id del " + campo + " Es requerido y debe ser positivo");
    }

    private static void validarFecha(LocalDateTime fechaCita) {
        if (fechaCita == null || !fechaCita.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Fecha de cita la cita es requerida y debe ser futura");
        }
    }

    public static void validarDatos(Long idPaciente, Long idMedico,
                                    LocalDateTime fechaCita, String sintomas) {
        validarId(idPaciente, "paciente");
        validarId(idMedico, "médico");
        validarFecha(fechaCita);
        StringCustomUtils.validarTamanio(sintomas, 20, 500,
                "Los síntomas son requeridos y deben ser entre 20 y 500 caracteres");
    }

    private void validarNoEliminado() {
        if (this.estadoRegistro == EstadoRegistro.ELIMINADO) {
            throw new IllegalArgumentException("La cita ya está eliminada");
        }
    }


    private void validarEliminacionPermitida() {
        validarNoEliminado();

        if (!estadoCita.isEliminable())
            throw new IllegalStateException("La cita con estado " + estadoCita + " no puede eliminarse");
    }

    private void validarActualizacionPermitida() {
        validarNoEliminado();

        if (!estadoCita.isActualizable())
            throw new IllegalStateException("La cita con estado " + estadoCita + " no puede eliminarse");
    }

    public void eliminar() {

        validarEliminacionPermitida();

        this.estadoRegistro = EstadoRegistro.ELIMINADO;
    }

    public void actualizar(Long idPaciente, Long idMedico, LocalDateTime fechaCita, String sintomas) {
        validarDatos(idPaciente, idMedico, fechaCita, sintomas);

        validarActualizacionPermitida();

        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
        this.fechaCita = fechaCita;
        this.sintomas = sintomas;
    }

    public void actualizarEstadoCita(EstadoCita nuevoEstado) {

        validarActualizacionPermitida();

        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El estado de la cita es requerido");
        }

        if (!estadoCita.puedeCambiarA(nuevoEstado))
            throw new IllegalStateException("La cita con estado " + estadoCita + " solo puede cambiar a: " + estadoCita.puedeCambiar());

        this.estadoCita = nuevoEstado;
    }

    public static Cita crear(
            Long idPaciente, Long idMedico,
            LocalDateTime fechaCita, String sintomas
    ) {
        validarDatos(idPaciente, idMedico, fechaCita, sintomas);
        return Cita.builder()
                .idPaciente(idPaciente)
                .idMedico(idMedico)
                .fechaCita(fechaCita)
                .sintomas(sintomas.trim())
                .estadoCita(EstadoCita.PENDIENTE)
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .build();
    }
}
