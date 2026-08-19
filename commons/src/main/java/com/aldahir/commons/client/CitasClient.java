package com.aldahir.commons.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "citas")
public interface CitasClient {

    @GetMapping("/medico/{idMedico}/citas")
    Integer obtenerCitasActivasPorMedico(@PathVariable Long idMedico);
    @GetMapping("/paciente/{idPaciente}/citas")
    Integer obtenerCitasActivasPorPaciente(@PathVariable Long idPaciente);

}