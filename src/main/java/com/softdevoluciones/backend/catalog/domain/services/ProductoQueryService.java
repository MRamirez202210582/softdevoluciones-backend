package com.softdevoluciones.backend.catalog.domain.services;

import com.softdevoluciones.backend.catalog.domain.model.aggregates.Producto;
import com.softdevoluciones.backend.catalog.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface ProductoQueryService {

    List<Producto> handle(GetAllProductosQuery query);

    Optional<Producto> handle(GetProductoByIdQuery query);
}