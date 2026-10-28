package com.softdevoluciones.backend.identity.domain.services;

import com.softdevoluciones.backend.identity.domain.model.aggregate.Usuario;
import com.softdevoluciones.backend.identity.domain.model.commands.RegisterCommand;

public interface UsuarioCommandService {

    Usuario handle(RegisterCommand command);
}