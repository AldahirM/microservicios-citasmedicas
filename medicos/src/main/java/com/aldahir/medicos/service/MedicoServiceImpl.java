package com.aldahir.medicos.service;

import com.aldahir.commons.client.CitasClient;
import com.aldahir.commons.dto.medicos.MedicoRequest;
import com.aldahir.commons.dto.medicos.MedicoResponse;
import com.aldahir.commons.enums.DisponibilidadMedico;
import com.aldahir.commons.enums.EspecialidadMedico;
import com.aldahir.commons.enums.EstadoRegistro;
import com.aldahir.commons.exceptions.RecursoNoEncontradoException;
import com.aldahir.medicos.entity.Medico;
import com.aldahir.medicos.mapper.MedicoMapper;
import com.aldahir.medicos.repository.MedicoRepository;
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
public class MedicoServiceImpl implements MedicoService {

    private final MedicoRepository medicoRepository;

    private final MedicoMapper medicoMapper;

    private final CitasClient citasClient;

    @Override
    @Transactional(readOnly = true)
    public List<MedicoResponse> listar() {
        log.info("Listando todos los medicos activos");

        return medicoRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO)
                .stream()
                .map(medicoMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MedicoResponse obtenerPorId(Long id) {
        return medicoMapper.entidadAResponse(obtenerMedicoActivoOException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public MedicoResponse obtenerMedicoPorIdSinEstado(Long id) {

        log.info("Obteniendo médico sin estado");

        return medicoMapper.entidadAResponse(medicoRepository
                .findById(id)
                .orElseThrow(() -> new
                        RecursoNoEncontradoException("Médico sin estado no encontrado con id: " + id)));
    }

    @Override
    public void actualizarDisponibilidadMedico(Long idMedico, Long idDisponibilidad) {
        Medico medico = obtenerMedicoActivoOException(idMedico);

        if (Objects.equals(medico.getDisponibilidadMedico().getCodigo(), idDisponibilidad)) {
            return;
        }

        log.info("Actualizando disponibilidad del médico con id: {}", idMedico);

        DisponibilidadMedico nuevaDisponibilidad = DisponibilidadMedico.obtenerDisponibilidadMedicoPorCodigo(idDisponibilidad);
        DisponibilidadMedico anteriorDisponibilidad = DisponibilidadMedico.
                obtenerDisponibilidadMedicoPorCodigo(medico.getDisponibilidadMedico().getCodigo());

        medico.actualizarDisponibilidad(nuevaDisponibilidad);

        log.info("Disponibilidad del médico con id {} cambió de {} a {}", idMedico, anteriorDisponibilidad, nuevaDisponibilidad);
    }

    @Override
    public MedicoResponse registrar(MedicoRequest request) {

        log.info("Registrando nuevo medico {}", request.nombre());

        validarDatosUnicos(request);

        Medico medico = medicoMapper.requestAEntidad(request);

        medico.actualizarEspecialidad(
                EspecialidadMedico.obtenerEspecialidadMedicoPorCodigo(request.idEspecialidad()));

        medicoRepository.save(medico);

        log.info("Nuevo médico registrado: {}", request.nombre());

        return medicoMapper.entidadAResponse(medico);
    }

    @Override
    public MedicoResponse actualizar(MedicoRequest request, Long id) {

        Medico medico = obtenerMedicoActivoOException(id);

        log.info("Actualizando médico con id: {}", id);

        if (citasClient.obtenerCitasActivasPorMedico(id) > 0) {
            throw new IllegalStateException("No se puede actualizar un médico con citas activas");
        }

        validarCambiosUnicos(request, id);

        medico.actualizar(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.edad(),
                request.email(),
                request.telefono(),
                request.cedulaProfesional(),
                EspecialidadMedico.obtenerEspecialidadMedicoPorCodigo(request.idEspecialidad())
        );

        log.info("Médico actualizado correctamente");

        return medicoMapper.entidadAResponse(medico);
    }

    @Override
    public void eliminar(Long id) {
        Medico medico = obtenerMedicoActivoOException(id);

        if (citasClient.obtenerCitasActivasPorMedico(id) > 0) {
            throw new IllegalStateException("No se puede eliminar un médico con citas activas");
        }

        log.info("Eliminando médico con id: {}", id);

        medico.eliminar();
    }

    private Medico obtenerMedicoActivoOException(Long id) {
        log.info("Buscando médico activo con id {}", id);

        return medicoRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException("Medico activo no encontrado"));
    }

    private void validarDatosUnicos(MedicoRequest request) {
        log.info("Validando email único...");

        if (medicoRepository.existsByEmailIgnoreCaseAndEstadoRegistro(request.email(), EstadoRegistro.ACTIVO))
            throw new IllegalArgumentException("Ya existe un médico activo registrado con el email: " + request.email());

        if (medicoRepository.existsByTelefonoAndEstadoRegistro(request.telefono(), EstadoRegistro.ACTIVO))
            throw new IllegalArgumentException("Ya existe un médico activo registrado con el número: " + request.telefono());

        if (medicoRepository.existsByCedulaProfesionalIgnoreCaseAndEstadoRegistro(request.cedulaProfesional(), EstadoRegistro.ACTIVO))
            throw new IllegalArgumentException("Ya existe un médico activo registrado con cedula: " + request.cedulaProfesional());
    }

    private void validarCambiosUnicos(MedicoRequest request, Long id) {
        log.info("Validando cambio en email único...");

        if (medicoRepository.existsByEmailIgnoreCaseAndEstadoRegistroAndIdNot(request.email(), EstadoRegistro.ACTIVO, id))
            throw new IllegalArgumentException("Ya existe un médico activo registrado con el email: " + request.email());

        log.info("Validando cambio en teléfono único...");

        if (medicoRepository.existsByTelefonoAndEstadoRegistroAndIdNot(request.telefono(), EstadoRegistro.ACTIVO, id))
            throw new IllegalArgumentException("Ya existe un médico activo registrado con el número: " + request.telefono());

        log.info("Validando cambio en cédula profesional única...");
        if (medicoRepository.existsByCedulaProfesionalIgnoreCaseAndEstadoRegistroAndIdNot(request.cedulaProfesional(), EstadoRegistro.ACTIVO, id))
            throw new IllegalArgumentException("Ya existe un médico activo registrado con cedula: " + request.cedulaProfesional());
    }
}
