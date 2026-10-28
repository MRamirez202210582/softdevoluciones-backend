package com.softdevoluciones.backend.shopping.domain.model.commands;

public record DeleteCarritoCommand(
        Long usuarioId,
        Long productoId
) {}