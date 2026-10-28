
package com.softdevoluciones.backend.shopping.interfaces.rest.transform;

import com.softdevoluciones.backend.shopping.domain.model.aggregates.Carrito;
import com.softdevoluciones.backend.shopping.interfaces.rest.resources.*;

import java.math.BigDecimal;
import java.util.List;

public class CarritoResourceFromEntityAssembler {

    public static CarritoResource toResourceFromEntity(Carrito carrito) {

        List<CarritoItemResource> productos = carrito.getDetalles()
                .stream()
                .map(detalle -> {

                    var producto = detalle.getProducto();

                    BigDecimal subtotal = producto.getPrecio()
                            .multiply(BigDecimal.valueOf(detalle.getCantidad()));

                    return new CarritoItemResource(
                            producto.getId(),
                            producto.getNombre(),
                            producto.getImagenUrl(),
                            producto.getPrecio(),
                            detalle.getCantidad(),
                            subtotal
                    );
                })
                .toList();

        BigDecimal total = productos.stream()
                .map(CarritoItemResource::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CarritoResource(
                carrito.getId(),
                carrito.getUsuario().getId(),
                productos,
                total
        );
    }
}
