
package com.softdevoluciones.backend.catalog.interfaces.rest.transform;

import com.softdevoluciones.backend.catalog.domain.model.aggregates.Producto;
import com.softdevoluciones.backend.catalog.interfaces.rest.resources.ProductoResource;

public class ProductoResourceFromEntityAssembler {

    public static ProductoResource toResourceFromEntity(Producto producto) {

        return new ProductoResource(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getImagenUrl(),
                producto.getActivo()
        );
    }
}
