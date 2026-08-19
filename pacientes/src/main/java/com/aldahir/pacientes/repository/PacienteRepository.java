package com.aldahir.pacientes.repository;

import com.aldahir.commons.enums.EstadoRegistro;
import com.aldahir.pacientes.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    List<Paciente> findByEstadoRegistro(EstadoRegistro estadoRegistro);

    Optional<Paciente> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);

    boolean existsByEmailIgnoreCaseAndEstadoRegistro(String email, EstadoRegistro estadoRegistro);
    boolean existsByEmailIgnoreCaseAndEstadoRegistroAndIdNot(String email, EstadoRegistro estadoRegistro, Long id);

    boolean existsByTelefonoIgnoreCaseAndEstadoRegistro(String telefono, EstadoRegistro estadoRegistro);
    boolean existsByTelefonoIgnoreCaseAndEstadoRegistroAndIdNot(String telefono, EstadoRegistro estadoRegistro, Long id);


}
