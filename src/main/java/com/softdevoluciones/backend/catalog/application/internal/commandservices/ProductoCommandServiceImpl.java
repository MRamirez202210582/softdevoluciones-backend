
package com.softdevoluciones.backend.catalog.application.internal.commandservices;

import com.softdevoluciones.backend.catalog.domain.model.aggregates.Producto;
import com.softdevoluciones.backend.catalog.domain.model.commands.*;
import com.softdevoluciones.backend.catalog.domain.services.ProductoCommandService;
import com.softdevoluciones.backend.catalog.infrastructure.persistence.jpa.repositories.ProductoRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProductoCommandServiceImpl implements ProductoCommandService {

    private final ProductoRepository productoRepository;

    @Override
    @Transactional
    public Producto handle(CreateProductoCommand command) {

        validarProducto(
                command.nombre(),
                command.precio(),
                command.stock()
        );

        Producto producto = Producto.builder()
                .nombre(command.nombre().trim())
                .descripcion(command.descripcion())
                .precio(command.precio())
                .stock(command.stock())
                .imagenUrl(command.imagenUrl())
                .activo(true)
                .build();

        return productoRepository.save(producto);
    }

    @Override
    @Transactional
    public Producto handle(UpdateProductoCommand command) {

        Producto producto = productoRepository.findById(command.id())
                .orElseThrow(() ->
                        new NoSuchElementException("Producto no encontrado")
                );

        if (!Boolean.TRUE.equals(producto.getActivo())) {
            throw new IllegalArgumentException("El producto está inactivo");
        }

        validarProducto(
                command.nombre(),
                command.precio(),
                command.stock()
        );

        producto.setNombre(command.nombre().trim());
        producto.setDescripcion(command.descripcion());
        producto.setPrecio(command.precio());
        producto.setStock(command.stock());
        producto.setImagenUrl(command.imagenUrl());

        return productoRepository.save(producto);
    }

    @Override
    @Transactional
    public void handle(DeleteProductoCommand command) {

        Producto producto = productoRepository.findById(command.id())
                .orElseThrow(() ->
                        new NoSuchElementException("Producto no encontrado")
                );

        // Eliminación lógica para conservar historial de pedidos
        producto.setActivo(false);

        productoRepository.save(producto);
    }

    private void validarProducto(
            String nombre,
            BigDecimal precio,
            Integer stock
    ) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del producto es obligatorio"
            );
        }

        if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "El precio debe ser mayor que cero"
            );
        }

        if (stock == null || stock < 0) {
            throw new IllegalArgumentException(
                    "El stock no puede ser negativo"
            );
        }
    }
}
