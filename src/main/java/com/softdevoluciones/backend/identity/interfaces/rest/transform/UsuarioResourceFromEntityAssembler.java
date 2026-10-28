package com.softdevoluciones.backend.identity.interfaces.rest.transform;

import com.softdevoluciones.backend.identity.domain.model.aggregate.Usuario;
import com.softdevoluciones.backend.identity.interfaces.rest.resources.UsuarioResource;

public class UsuarioResourceFromEntityAssembler {

    public static UsuarioResource toResourceFromEntity(Usuario usuario) {
        return new UsuarioResource(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol().name()
        );
    }
}