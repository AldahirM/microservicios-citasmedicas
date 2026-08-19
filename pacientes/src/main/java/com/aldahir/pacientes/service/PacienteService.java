package com.aldahir.pacientes.service;

import com.aldahir.commons.dto.pacientes.PacienteRequest;
import com.aldahir.commons.dto.pacientes.PacienteResponse;
import com.aldahir.commons.service.CrudService;

public interface PacienteService extends CrudService<PacienteRequest, PacienteResponse> {

    PacienteResponse obtenerPorIdSinEstado(Long id);

}
