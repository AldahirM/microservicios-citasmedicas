package com.aldahir.pacientes.controller;

import com.aldahir.commons.controller.CommonController;
import com.aldahir.commons.dto.pacientes.PacienteRequest;
import com.aldahir.commons.dto.pacientes.PacienteResponse;
import com.aldahir.pacientes.service.PacienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PacienteController extends CommonController<PacienteRequest, PacienteResponse, PacienteService> {
    public PacienteController(PacienteService service) {
        super(service);
    }

    @GetMapping("/id-paciente/{id}")
    public ResponseEntity<PacienteResponse> obtenerPacientePor(@PathVariable Long id) {
        return ResponseEntity.ok(super.service.obtenerPorIdSinEstado(id));
    }

}
