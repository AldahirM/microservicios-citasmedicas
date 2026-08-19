package com.aldahir.auth.service;
import java.util.Set;

import com.aldahir.auth.dto.UsuarioRequest;
import com.aldahir.auth.dto.UsuarioResponse;

public interface UsuarioService {

    Set<UsuarioResponse> listar();

    UsuarioResponse registrar(UsuarioRequest request);

    UsuarioResponse eliminar(String username);
}