package com.aldahir.auth.dto;

public record CustomErrorResponse(
        int codigo,
        String mensaje
) { }