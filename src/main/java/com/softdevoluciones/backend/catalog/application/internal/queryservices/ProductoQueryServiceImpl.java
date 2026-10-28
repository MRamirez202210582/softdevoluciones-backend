
package com.softdevoluciones.backend.catalog.application.internal.queryservices;

import com.softdevoluciones.backend.catalog.domain.model.aggregates.Producto;
import com.softdevoluciones.backend.catalog.domain.model.queries.*;
import com.softdevoluciones.backend.catalog.domain.services.ProductoQueryService;
import com.softdevoluciones.backend.catalog.infrastructure.persistence.jpa.repositories.ProductoRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductoQueryServiceImpl implements ProductoQueryService {

    private final ProductoRepository productoRepository;

    @Override
    public List<Producto> handle(GetAllProductosQuery query) {
        return productoRepository.findByActivoTrue();
    }

    @Override
    public Optional<Producto> handle(GetProductoByIdQuery query) {
        return productoRepository.findById(query.id())
                .filter(producto -> Boolean.TRUE.equals(producto.getActivo()));
    }
}
