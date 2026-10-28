package com.softdevoluciones.backend.catalog.domain.model.commands;

import java.math.BigDecimal;

public record UpdateProductoCommand(
        Long id,
        String nombre,
        String descripcion,
        BigDecimal precio,
        Integer stock,
        String imagenUrl
) {}