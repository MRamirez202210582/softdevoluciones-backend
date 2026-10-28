package com.softdevoluciones.backend.identity.interfaces.rest.resources;

import com.softdevoluciones.backend.identity.domain.model.valueobjects.Rol;

public record UsuarioAdminResource(
        Long id,
        String nombre,
        String email,
        Rol rol
) {
}