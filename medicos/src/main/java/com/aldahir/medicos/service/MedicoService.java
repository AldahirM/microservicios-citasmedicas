package com.aldahir.medicos.service;

import com.aldahir.commons.dto.medicos.MedicoRequest;
import com.aldahir.commons.dto.medicos.MedicoResponse;
import com.aldahir.commons.service.CrudService;
import com.aldahir.medicos.entity.Medico;

public interface MedicoService extends CrudService<MedicoRequest, MedicoResponse> {
    MedicoResponse obtenerMedicoPorIdSinEstado(Long id);
    void actualizarDisponibilidadMedico(Long idMedico, Long idDisponibilidad);
}
