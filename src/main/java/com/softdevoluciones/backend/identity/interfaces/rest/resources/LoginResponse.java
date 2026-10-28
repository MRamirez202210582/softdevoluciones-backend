package com.softdevoluciones.backend.identity.interfaces.rest.resources;

public record LoginResponse(
        String token,
        UsuarioResource usuario
) {}