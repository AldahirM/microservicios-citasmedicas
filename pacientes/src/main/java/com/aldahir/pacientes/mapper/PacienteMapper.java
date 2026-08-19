package com.aldahir.pacientes.mapper;

import com.aldahir.commons.dto.pacientes.PacienteRequest;
import com.aldahir.commons.dto.pacientes.PacienteResponse;
import com.aldahir.commons.mapper.CommonMapper;
import com.aldahir.pacientes.entity.Paciente;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper implements CommonMapper<PacienteRequest, PacienteResponse, Paciente> {
    @Override
    public Paciente requestAEntidad(PacienteRequest request) {
        return Paciente.builder()
                .nombre(request.nombre())
                .apellidoPaterno(request.apellidoPaterno())
                .apellidoMaterno(request.apellidoMaterno())
                .edad(request.edad())
                .email(request.email())
                .peso(request.peso())
                .estatura(request.estatura())
                .telefono(request.telefono())
                .direccion(request.direccion())
                .build();
    }


    @Override
    public PacienteResponse entidadAResponse(Paciente entidad) {
        return new PacienteResponse(
                entidad.getId(),
                String.join(" ", entidad.getNombre(), entidad.getApellidoPaterno(), entidad.getApellidoMaterno()),
                entidad.getEdad(),
                entidad.getPeso(),
                entidad.getEstatura(),
                entidad.getImc(),
                entidad.getEmail(),
                entidad.getTelefono(),
                entidad.getDireccion(),
                entidad.getNumExpediente()
        );
    }
}
