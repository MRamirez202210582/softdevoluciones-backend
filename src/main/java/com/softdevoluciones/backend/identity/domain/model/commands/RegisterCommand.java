package com.softdevoluciones.backend.identity.domain.model.commands;

public record RegisterCommand(
        String nombre,
        String email,
        String password
) {}