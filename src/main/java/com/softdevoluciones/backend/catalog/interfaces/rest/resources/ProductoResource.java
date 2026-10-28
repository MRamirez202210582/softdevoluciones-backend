
package com.softdevoluciones.backend.catalog.interfaces.rest.resources;

import java.math.BigDecimal;

public record ProductoResource(
        Long id,
        String nombre,
        String descripcion,
        BigDecimal precio,
        Integer stock,
        String imagenUrl,
        Boolean activo
) {}
