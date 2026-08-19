package com.aldahir.medicos.mapper;

import com.aldahir.commons.dto.medicos.MedicoRequest;
import com.aldahir.commons.dto.medicos.MedicoResponse;
import com.aldahir.commons.enums.DisponibilidadMedico;
import com.aldahir.commons.enums.EstadoRegistro;
import com.aldahir.commons.mapper.CommonMapper;
import com.aldahir.medicos.entity.Medico;
import org.springframework.stereotype.Component;

@Component
public class MedicoMapper implements CommonMapper<MedicoRequest, MedicoResponse, Medico> {
    @Override
    public Medico requestAEntidad(MedicoRequest request) {
        return Medico.builder()
                .nombre(request.nombre())
                .apellidoPaterno(request.apellidoPaterno().trim())
                .apellidoMaterno(request.apellidoMaterno().trim())
                .edad(request.edad())
                .email(request.email().trim().toLowerCase())
                .telefono(request.telefono().trim())
                .cedulaProfesional(request.cedulaProfesional().trim())
                .disponibilidadMedico(DisponibilidadMedico.DISPONIBLE)
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .build();
    }

    @Override
    public MedicoResponse entidadAResponse(Medico entidad) {
        return new MedicoResponse(
                entidad.getId(),
                String.join(" ", entidad.getNombre(), entidad.getApellidoPaterno(), entidad.getApellidoMaterno()),
                entidad.getEdad(),
                entidad.getEmail(),
                entidad.getTelefono(),
                entidad.getCedulaProfesional(),
                entidad.getEspecialidadMedico().getDescripcion(),
                entidad.getDisponibilidadMedico().getDescripcion(),
                entidad.getDisponibilidadMedico().getCodigo()
        );
    }
}
