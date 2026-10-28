
package com.softdevoluciones.backend.catalog.interfaces.rest.resources;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record SaveProductoResource(

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        String descripcion,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor que cero")
        BigDecimal precio,

        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock no puede ser negativo")
        Integer stock,

        String imagenUrl
) {}
