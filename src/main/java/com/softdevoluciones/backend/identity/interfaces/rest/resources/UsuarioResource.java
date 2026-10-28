package com.softdevoluciones.backend.identity.interfaces.rest.resources;

public record UsuarioResource(
        Long id,
        String nombre,
        String email,
        String rol
) {}