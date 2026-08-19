package com.aldahir.medicos.repository;

import com.aldahir.commons.dto.medicos.MedicoResponse;
import com.aldahir.commons.enums.EstadoRegistro;
import com.aldahir.medicos.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico,Long> {
    List<Medico> findByEstadoRegistro(EstadoRegistro estadoRegistro);
    Optional<Medico> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);
    boolean existsByEmailIgnoreCaseAndEstadoRegistro(String email, EstadoRegistro estadoRegistro);
    boolean existsByEmailIgnoreCaseAndEstadoRegistroAndIdNot(String email, EstadoRegistro estadoRegistro, Long idMedico);
    boolean existsByTelefonoAndEstadoRegistro(String telefono, EstadoRegistro estadoRegistro);
    boolean existsByTelefonoAndEstadoRegistroAndIdNot(String telefono, EstadoRegistro estadoRegistro, Long idMedico);
    boolean existsByCedulaProfesionalIgnoreCaseAndEstadoRegistro(String cedulaProfesional, EstadoRegistro estadoRegistro);
    boolean existsByCedulaProfesionalIgnoreCaseAndEstadoRegistroAndIdNot(String cedulaProfesional, EstadoRegistro estadoRegistro, Long idMedico);

}
