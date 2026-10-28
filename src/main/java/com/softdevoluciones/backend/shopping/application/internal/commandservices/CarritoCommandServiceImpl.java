
package com.softdevoluciones.backend.shopping.application.internal.commandservices;

import com.softdevoluciones.backend.catalog.domain.model.aggregates.Producto;
import com.softdevoluciones.backend.catalog.infrastructure.persistence.jpa.repositories.ProductoRepository;
import com.softdevoluciones.backend.identity.domain.model.aggregate.Usuario;
import com.softdevoluciones.backend.identity.infrastructure.persistence.jpa.repositories.UsuarioRepository;
import com.softdevoluciones.backend.shopping.domain.model.aggregates.Carrito;
import com.softdevoluciones.backend.shopping.domain.model.aggregates.DetalleCarrito;
import com.softdevoluciones.backend.shopping.domain.model.commands.*;
import com.softdevoluciones.backend.shopping.domain.services.CarritoCommandService;
import com.softdevoluciones.backend.shopping.infrastructure.persistence.jpa.repositories.CarritoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CarritoCommandServiceImpl implements CarritoCommandService {

    private final CarritoRepository carritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    @Override
    @Transactional
    public Carrito handle(CreateCarritoCommand command) {

        if (command.cantidad() == null || command.cantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }

        Producto producto = obtenerProducto(command.productoId());

        Carrito carrito = obtenerOCrearCarrito(command.usuarioId());

        DetalleCarrito detalle = carrito.getDetalles()
                .stream()
                .filter(item -> item.getProducto().getId().equals(command.productoId()))
                .findFirst()
                .orElse(null);

        int cantidadActual = detalle == null ? 0 : detalle.getCantidad();
        long nuevaCantidad = (long) cantidadActual + command.cantidad();

        validarStock(producto, nuevaCantidad);

        if (detalle != null) {
            detalle.setCantidad((int) nuevaCantidad);
        } else {
            DetalleCarrito nuevoDetalle = DetalleCarrito.builder()
                    .producto(producto)
                    .cantidad(command.cantidad())
                    .build();

            carrito.agregarDetalle(nuevoDetalle);
        }

        return carritoRepository.save(carrito);
    }

    @Override
    @Transactional
    public Carrito handle(UpdateCarritoCommand command) {

        if (command.cantidad() == null || command.cantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }

        Carrito carrito = obtenerCarrito(command.usuarioId());

        DetalleCarrito detalle = buscarDetalle(carrito, command.productoId());

        Producto producto = obtenerProducto(command.productoId());

        validarStock(producto, command.cantidad());

        detalle.setCantidad(command.cantidad());

        return carritoRepository.save(carrito);
    }

    @Override
    @Transactional
    public Carrito handle(DeleteCarritoCommand command) {

        Carrito carrito = obtenerCarrito(command.usuarioId());

        DetalleCarrito detalle = buscarDetalle(carrito, command.productoId());

        carrito.eliminarDetalle(detalle);

        return carritoRepository.save(carrito);
    }

    @Override
    @Transactional
    public Carrito handle(ClearCarritoCommand command) {

        Carrito carrito = obtenerCarrito(command.usuarioId());

        carrito.vaciar();

        return carritoRepository.save(carrito);
    }

    private Carrito obtenerOCrearCarrito(Long usuarioId) {

        return carritoRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> {
                    Usuario usuario = usuarioRepository.findById(usuarioId)
                            .orElseThrow(() ->
                                    new NoSuchElementException("Usuario no encontrado")
                            );

                    return carritoRepository.save(
                            Carrito.builder()
                                    .usuario(usuario)
                                    .build()
                    );
                });
    }

    private Carrito obtenerCarrito(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() ->
                        new NoSuchElementException("Carrito no encontrado")
                );
    }

    private Producto obtenerProducto(Long productoId) {

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() ->
                        new NoSuchElementException("Producto no encontrado")
                );

        if (!Boolean.TRUE.equals(producto.getActivo())) {
            throw new IllegalArgumentException("El producto no está disponible");
        }

        return producto;
    }

    private DetalleCarrito buscarDetalle(Carrito carrito, Long productoId) {

        return carrito.getDetalles()
                .stream()
                .filter(item -> item.getProducto().getId().equals(productoId))
                .findFirst()
                .orElseThrow(() ->
                        new NoSuchElementException("Producto no encontrado en el carrito")
                );
    }

    private void validarStock(Producto producto, long cantidad) {

        if (producto.getStock() == null || cantidad > producto.getStock()) {
            throw new IllegalArgumentException("Stock insuficiente");
        }
    }
}
