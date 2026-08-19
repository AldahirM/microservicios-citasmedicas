package com.aldahir.auth.service;
import com.aldahir.auth.dto.LoginRequest;
import com.aldahir.auth.dto.TokenResponse;

public interface AuthService {

    TokenResponse autenticar(LoginRequest request) throws Exception;
}