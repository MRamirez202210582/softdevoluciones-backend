
package com.softdevoluciones.backend.shopping.interfaces.rest.resources;

import java.math.BigDecimal;
import java.util.List;

public record CarritoResource(
        Long id,
        Long usuarioId,
        List<CarritoItemResource> productos,
        BigDecimal total
) {}
