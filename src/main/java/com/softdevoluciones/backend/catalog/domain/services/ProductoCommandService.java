package com.softdevoluciones.backend.catalog.domain.services;

import com.softdevoluciones.backend.catalog.domain.model.aggregates.Producto;
import com.softdevoluciones.backend.catalog.domain.model.commands.*;

public interface ProductoCommandService {

    Producto handle(CreateProductoCommand command);

    Producto handle(UpdateProductoCommand command);

    void handle(DeleteProductoCommand command);
}