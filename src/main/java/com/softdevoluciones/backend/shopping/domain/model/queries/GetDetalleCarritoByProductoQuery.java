
package com.softdevoluciones.backend.shopping.domain.model.queries;

public record GetDetalleCarritoByProductoQuery(
        Long carritoId,
        Long productoId
) {}
