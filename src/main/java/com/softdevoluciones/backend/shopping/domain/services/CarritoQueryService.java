
package com.softdevoluciones.backend.shopping.domain.services;

import com.softdevoluciones.backend.shopping.domain.model.aggregates.Carrito;
import com.softdevoluciones.backend.shopping.domain.model.queries.GetCarritoByUsuarioIdQuery;

import java.util.Optional;

public interface CarritoQueryService {

    Optional<Carrito> handle(GetCarritoByUsuarioIdQuery query);
}
