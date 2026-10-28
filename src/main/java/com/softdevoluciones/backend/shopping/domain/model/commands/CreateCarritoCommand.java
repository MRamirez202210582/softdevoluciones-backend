package com.softdevoluciones.backend.shopping.domain.model.commands;

public record CreateCarritoCommand(
        Long usuarioId,
        Long productoId,
        Integer cantidad
) {}