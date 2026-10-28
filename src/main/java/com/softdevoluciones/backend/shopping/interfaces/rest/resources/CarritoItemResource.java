
package com.softdevoluciones.backend.shopping.interfaces.rest.resources;

import java.math.BigDecimal;

public record CarritoItemResource(
        Long productoId,
        String nombre,
        String imagenUrl,
        BigDecimal precio,
        Integer cantidad,
        BigDecimal subtotal
) {}
