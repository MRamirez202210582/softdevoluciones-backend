package com.softdevoluciones.backend.shopping.domain.model.commands;

public record UpdateCarritoCommand(
        Long usuarioId,
        Long productoId,
        Integer cantidad
) {}