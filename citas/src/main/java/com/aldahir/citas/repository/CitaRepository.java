package com.aldahir.citas.repository;

import com.aldahir.citas.entity.Cita;
import com.aldahir.commons.enums.EstadoRegistro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    @Query("SELECT COUNT(*) FROM Cita WHERE idPaciente = :idPaciente AND (estadoCita = 'PENDIENTE' OR estadoCita = 'CONFIRMADA' OR estadoCita = 'EN_CURSO')")
    Integer countCitasActivasByIdPaciente(Long idPaciente);

    @Query("SELECT COUNT(*) FROM Cita WHERE idMedico = :idMedico AND (estadoCita = 'CONFIRMADA' OR estadoCita = 'EN_CURSO' )")
    Integer countCitasActivasByIdMedico(Long idMedico);

    List<Cita> findByEstadoRegistro(EstadoRegistro estadoRegistro);

    Optional<Cita> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);

}
