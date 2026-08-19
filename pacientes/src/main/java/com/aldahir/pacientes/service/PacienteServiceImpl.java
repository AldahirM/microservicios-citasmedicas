package com.aldahir.pacientes.service;

import com.aldahir.commons.client.CitasClient;
import com.aldahir.commons.dto.pacientes.PacienteRequest;
import com.aldahir.commons.dto.pacientes.PacienteResponse;
import com.aldahir.commons.enums.EstadoRegistro;
import com.aldahir.commons.exceptions.RecursoNoEncontradoException;
import com.aldahir.pacientes.entity.Paciente;
import com.aldahir.pacientes.mapper.PacienteMapper;
import com.aldahir.pacientes.repository.PacienteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;

    private final PacienteMapper pacienteMapper;

    private final CitasClient citaClient;

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> listar() {
        return pacienteRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO)
                .stream()
                .map(pacienteMapper::entidadAResponse)
                .toList();
    }

    @Override
    public PacienteResponse obtenerPorId(Long id) {
        return pacienteMapper.entidadAResponse(pacienteRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new
                        RecursoNoEncontradoException("Paciente no encontrado con id: " + id)));
    }

    @Override
    public PacienteResponse registrar(PacienteRequest request) {

        validarDatosUnicos(request);

        Paciente paciente = pacienteMapper.requestAEntidad(request);

        paciente.agregarNumExpediente(generarNumeroExpediente(request.telefono()));

        paciente.agregarEstadoRegistro(EstadoRegistro.ACTIVO);

        paciente.calcularIMC();

        pacienteRepository.save(paciente);

        return pacienteMapper.entidadAResponse(paciente);
    }

    @Override
    public PacienteResponse actualizar(PacienteRequest request, Long id) {

        Paciente paciente = obtenerPacienteOException(id);

        validarDatosUnicos(request);

        paciente.agregarEstadoRegistro(EstadoRegistro.ACTIVO);

        paciente.agregarNumExpediente(generarNumeroExpediente(request.telefono()));

        paciente.actualizarDatos(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.edad(),
                request.peso(),
                request.estatura(),
                request.email(),
                request.telefono(),
                request.direccion()
        );

        paciente.calcularIMC();

        pacienteRepository.save(paciente);

        return pacienteMapper.entidadAResponse(paciente);

    }

    @Override
    public void eliminar(Long id) {

        if (citaClient.obtenerCitasActivasPorPaciente(id) > 0) {
            log.info("Citas del paciente: {}", citaClient.obtenerCitasActivasPorPaciente(id));
            throw new IllegalStateException("No se puede eliminar un paciente con citas pendientes");
        }

        Paciente paciente = obtenerPacienteOException(id);

        paciente.agregarEstadoRegistro(EstadoRegistro.ELIMINADO);

        pacienteRepository.save(paciente);

    }

    @Override
    public PacienteResponse obtenerPorIdSinEstado(Long id) {
        return pacienteMapper.entidadAResponse(obtenerPacienteOException(id));
    }


    private String generarNumeroExpediente(String telefono) {
        StringBuilder sb = new StringBuilder();
        log.info("Generando número de expediente para el teléfono: {}", telefono);

        for (int i = 0; i < telefono.length(); i++) {
            sb.append(telefono.charAt(i)).append("X");
        }
        log.info(sb.toString());
        return sb.toString();
    }

    private Paciente obtenerPacienteOException(Long id) {
        log.info("Buscando médico activo con id {}", id);

        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado"));
    }

    private void validarDatosUnicos(PacienteRequest request) {
        if (pacienteRepository.existsByEmailIgnoreCaseAndEstadoRegistro(request.email(), EstadoRegistro.ACTIVO)) {
            throw new IllegalArgumentException("El email ya está en uso por otro paciente");
        }
        if (pacienteRepository.existsByTelefonoIgnoreCaseAndEstadoRegistro(request.telefono(), EstadoRegistro.ACTIVO)) {
            throw new IllegalArgumentException("El teléfono ya está en uso por otro paciente");
        }
    }

    private void validarCambiosUnicos(PacienteRequest request, Long id) {
        if (pacienteRepository.existsByEmailIgnoreCaseAndEstadoRegistroAndIdNot(request.email(), EstadoRegistro.ACTIVO, id)) {
            throw new IllegalArgumentException("El email ya está en uso por otro paciente");
        }
        if (pacienteRepository.existsByTelefonoIgnoreCaseAndEstadoRegistroAndIdNot(request.telefono(), EstadoRegistro.ACTIVO, id)) {
            throw new IllegalArgumentException("El teléfono ya está en uso por otro paciente");
        }
    }

}
