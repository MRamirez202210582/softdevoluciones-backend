package com.softdevoluciones.backend.catalog.domain.model.commands;

import java.math.BigDecimal;

public record CreateProductoCommand(
        String nombre,
        String descripcion,
        BigDecimal precio,
        Integer stock,
        String imagenUrl
) {}