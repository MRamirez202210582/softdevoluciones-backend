package com.softdevoluciones.backend.shopping.domain.services;

import com.softdevoluciones.backend.shopping.domain.model.aggregates.Carrito;
import com.softdevoluciones.backend.shopping.domain.model.commands.*;

public interface CarritoCommandService {

    Carrito handle(CreateCarritoCommand command);

    Carrito handle(UpdateCarritoCommand command);

    Carrito handle(DeleteCarritoCommand command);

    Carrito handle(ClearCarritoCommand command);
}