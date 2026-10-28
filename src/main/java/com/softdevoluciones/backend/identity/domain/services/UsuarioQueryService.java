package com.softdevoluciones.backend.identity.domain.services;

import com.softdevoluciones.backend.identity.domain.model.aggregate.Usuario;
import com.softdevoluciones.backend.identity.domain.model.queries.GetUsuarioByEmailQuery;
import com.softdevoluciones.backend.identity.domain.model.queries.GetUsuarioByIdQuery;

import java.util.Optional;

public interface UsuarioQueryService {

    Optional<Usuario> handle(GetUsuarioByEmailQuery query);

    Optional<Usuario> handle(GetUsuarioByIdQuery query);
}