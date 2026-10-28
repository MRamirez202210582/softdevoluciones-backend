package com.softdevoluciones.backend.identity.domain.model.commands;

public record LoginCommand(
        String email,
        String password
) {}