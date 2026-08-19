package com.aldahir.citas.controller;

import com.aldahir.citas.dto.CitaRequest;
import com.aldahir.citas.dto.CitaResponse;
import com.aldahir.citas.service.CitaService;
import com.aldahir.commons.controller.CommonController;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CitaController extends CommonController<CitaRequest, CitaResponse, CitaService> {
    public CitaController(CitaService service) {
        super(service);
    }

    @PatchMapping("/{idCita}/estado/{idEstado}")
    public ResponseEntity<Void> actualizarEstadoCita(
            @PathVariable @Positive(message = "El idCita debe ser positivo") Long idCita,
            @PathVariable @Positive(message = "El idEstado debe ser positivo") Long idEstado
    ) {
        service.actualizarEstadoCita(idCita, idEstado);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/medico/{idMedico}/citas")
    public ResponseEntity<Integer> contarCitasPorMedico(
            @PathVariable @Positive(message = "El idMedico debe ser positivo") Long idMedico
    ) {
        return ResponseEntity.ok(service.contarCitasPorMedico(idMedico));
    }
    @GetMapping("/paciente/{idPaciente}/citas")
    public ResponseEntity<Integer> contarCitasPorPaciente(
            @PathVariable @Positive(message = "El idPaciente debe ser positivo") Long idPaciente
    ) {
        return ResponseEntity.ok(service.contarCitasPorPaciente(idPaciente));
    }

}
