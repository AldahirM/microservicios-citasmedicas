package com.aldahir.citas.service;

import com.aldahir.citas.dto.CitaRequest;
import com.aldahir.citas.dto.CitaResponse;
import com.aldahir.commons.service.CrudService;

public interface CitaService extends CrudService<CitaRequest, CitaResponse> {
    void actualizarEstadoCita(Long idCita, Long idEstadoCita);

    Integer contarCitasPorMedico(Long idMedico);

    Integer contarCitasPorPaciente(Long idPaciente);
}
