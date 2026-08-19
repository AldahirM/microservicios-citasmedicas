package com.aldahir.citas.service;

import com.aldahir.citas.dto.CitaRequest;
import com.aldahir.citas.dto.CitaResponse;
import com.aldahir.citas.entity.Cita;
import com.aldahir.citas.enums.EstadoCita;
import com.aldahir.citas.mapper.CitaMapper;
import com.aldahir.citas.repository.CitaRepository;
import com.aldahir.commons.client.MedicoClient;
import com.aldahir.commons.client.PacienteClient;
import com.aldahir.commons.dto.medicos.MedicoResponse;
import com.aldahir.commons.dto.pacientes.PacienteResponse;
import com.aldahir.commons.enums.DisponibilidadMedico;
import com.aldahir.commons.enums.EstadoRegistro;
import com.aldahir.commons.exceptions.RecursoNoEncontradoException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;

    private final CitaMapper citaMapper;

    private final MedicoClient medicoClient;

    private final PacienteClient pacienteClient;

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> listar() {

        log.info("Listando todas las citas activas");

        return citaRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(cita -> citaMapper.entidadAResponse(
                        cita,
                        obtenerPacienteSinEstado(cita.getIdPaciente()),
                        obtenerMedicoSinEstado(cita.getIdMedico())
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CitaResponse obtenerPorId(Long id) {

        Cita cita = obtenerCitaOException(id);

        return citaMapper.entidadAResponse(
                cita,
                obtenerPacienteSinEstado(cita.getIdPaciente()),
                obtenerMedicoSinEstado(cita.getIdMedico())
        );
    }

    @Override
    public CitaResponse registrar(CitaRequest request) {
        log.info("Registrando cita activa");

        MedicoResponse medico = obtenerMedicoActivo(request.idMedico());

        validarMedicoDisponible(medico);

        PacienteResponse paciente = obtenerPacienteActivo(request.idPaciente());

        validarNumeroCitasPaciente(paciente.id());

        Cita cita = citaMapper.requestAEntidad(request);

        citaRepository.save(cita);

        log.info("Cita registrada exitósamente");

        return citaMapper.entidadAResponse(
                cita,
                paciente,
                medico
        );
    }

    @Override
    public CitaResponse actualizar(CitaRequest request, Long id) {

        Cita cita = obtenerCitaOException(id);

        MedicoResponse medico = obtenerMedicoActivo(request.idMedico());

        validarMedicoDisponible(medico);

        PacienteResponse paciente = obtenerPacienteActivo(request.idPaciente());

        if (!Objects.equals(cita.getIdMedico(), request.idMedico())) {
            medicoClient.actualizarDisponibilidadMedico(cita.getIdMedico(), DisponibilidadMedico.DISPONIBLE.getCodigo());
            medicoClient.actualizarDisponibilidadMedico(request.idMedico(), DisponibilidadMedico.DISPONIBLE.getCodigo());
        }

        cita.actualizar(
                request.idPaciente(),
                request.idMedico(),
                request.fechaCita(),
                request.sintomas()
        );

        return citaMapper.entidadAResponse(
                cita,
                paciente,
                obtenerMedicoSinEstado(cita.getIdMedico())
        );
    }

    @Override
    public void eliminar(Long id) {
        Cita cita = obtenerCitaOException(id);

        log.info("Eliminando cita con id: {}", id);

        cita.eliminar();

        log.info("Cita con id {} ha sido marcada como eliminada", id);
    }

    @Override
    public void actualizarEstadoCita(Long idCita, Long idEstadoCita) {

        Cita cita = obtenerCitaOException(idCita);

        Long idDisponibilidadMedico = validarDisponibilidadMedico(idEstadoCita);

        log.info("Estado cita: {}", EstadoCita.obtenerEstadoCitaPorCodigo(idEstadoCita));

        log.info("Actualizando estado de la cita con id: {}", idCita);

        cita.actualizarEstadoCita(EstadoCita.obtenerEstadoCitaPorCodigo(idEstadoCita));

        medicoClient.actualizarDisponibilidadMedico(cita.getIdMedico(), idDisponibilidadMedico);

        log.info("Estado de la cita {} actualizando correctamente", idCita);
    }

    @Override
    public Integer contarCitasPorMedico(Long idMedico) {
        return citaRepository.countByEstadoRegistroAndIdMedicoAndIdCitaNot(idMedico);
    }

    @Override
    public Integer contarCitasPorPaciente(Long idPaciente) {
        return citaRepository.countByEstadoRegistroAndIdPaciente(idPaciente);
    }

    private MedicoResponse obtenerMedicoActivo(Long id) {
        log.info("Buscando médico activo con id {} en el servicio remoto...", id);

        return medicoClient.obtenerMedicoActivoPorId(id);
    }

    private MedicoResponse obtenerMedicoSinEstado(Long id) {
        log.info("Buscando médico sin estado con id {} en el servicio remoto...", id);

        return medicoClient.obtenerMedicoPorIdSinEstado(id);
    }

    private Cita obtenerCitaOException(Long id) {
        log.info("Buscando cita con id {}", id);

        return citaRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow
                        (() -> new RecursoNoEncontradoException("Cita no encontrada con id : " + id));
    }

    private PacienteResponse obtenerPacienteActivo(Long id) {
        log.info("Buscando paciente con id {}", id);

        return pacienteClient.obtenerPacienteActivoPorId(id);
    }

    private PacienteResponse obtenerPacienteSinEstado(Long id) {
        log.info("Buscando paciente sin estado con id {}", id);

        return pacienteClient.obtenerPacienteSinEstadoPorId(id);
    }

    private Long validarDisponibilidadMedico(Long idEstadoCita) {
        if (idEstadoCita.equals(EstadoCita.PENDIENTE.getCodigo()) || idEstadoCita.equals(EstadoCita.CONFIRMADA.getCodigo())) {
            return DisponibilidadMedico.NO_DISPONIBLE.getCodigo();
        }
        if (idEstadoCita.equals(EstadoCita.EN_CURSO.getCodigo())) {
            return DisponibilidadMedico.EN_CONSULTA.getCodigo();
        }
        return DisponibilidadMedico.DISPONIBLE.getCodigo();
    }

    private void validarMedicoDisponible(MedicoResponse medico) {
        if (!medico.idDisponibilidad().equals(DisponibilidadMedico.DISPONIBLE.getCodigo())) {
            throw new IllegalArgumentException("Medico no disponible");
        }
    }

    private void validarNumeroCitasPaciente(Long idPaciente) {
        Integer count = citaRepository.countByEstadoRegistroAndId(idPaciente);
        if (count > 0) {
            throw new IllegalArgumentException("El paciente no puede tener dos citas en curso");
        }
    }


}
