
package com.softdevoluciones.backend.ordering.application.internal.commandservices;

import com.softdevoluciones.backend.catalog.domain.model.aggregates.Producto;
import com.softdevoluciones.backend.catalog.infrastructure.persistence.jpa.repositories.ProductoRepository;
import com.softdevoluciones.backend.identity.domain.model.aggregate.Usuario;
import com.softdevoluciones.backend.identity.infrastructure.persistence.jpa.repositories.UsuarioRepository;
import com.softdevoluciones.backend.ordering.domain.model.aggregates.DetallePedido;
import com.softdevoluciones.backend.ordering.domain.model.aggregates.Pedido;
import com.softdevoluciones.backend.ordering.domain.model.commands.CreatePedidoCommand;
import com.softdevoluciones.backend.ordering.domain.model.commands.UpdatePedidoEstadoCommand;
import com.softdevoluciones.backend.ordering.domain.model.valueobjects.EstadoPedido;
import com.softdevoluciones.backend.ordering.domain.services.PedidoCommandService;
import com.softdevoluciones.backend.ordering.infrastructure.persistence.jpa.repositories.PedidoRepository;
import com.softdevoluciones.backend.shopping.domain.model.aggregates.Carrito;
import com.softdevoluciones.backend.shopping.infrastructure.persistence.jpa.repositories.CarritoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PedidoCommandServiceImpl implements PedidoCommandService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final CarritoRepository carritoRepository;

    @Override
    @Transactional
    public Pedido handle(CreatePedidoCommand command) {

        if (command == null || command.usuarioId() == null) {
            throw new IllegalArgumentException(
                    "El usuario es obligatorio"
            );
        }

        if (command.clienteNombre() == null ||
                command.clienteNombre().isBlank() ||
                command.celular() == null ||
                command.celular().isBlank() ||
                command.direccion() == null ||
                command.direccion().isBlank()) {

            throw new IllegalArgumentException(
                    "Todos los datos del cliente son obligatorios"
            );
        }

        Usuario usuario = usuarioRepository.findById(command.usuarioId())
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Usuario no encontrado"
                        )
                );

        Carrito carrito = carritoRepository
                .findByUsuarioId(command.usuarioId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El carrito está vacío"
                        )
                );

        if (carrito.getDetalles().isEmpty()) {
            throw new IllegalArgumentException(
                    "El carrito está vacío"
            );
        }

        Pedido pedido = Pedido.builder()
                .usuario(usuario)
                .clienteNombre(command.clienteNombre().trim())
                .celular(command.celular().trim())
                .direccion(command.direccion().trim())
                .fecha(LocalDateTime.now())
                .estado(EstadoPedido.PENDIENTE)
                .total(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (var item : carrito.getDetalles()) {

            Producto producto = productoRepository
                    .findByIdForUpdate(item.getProducto().getId())
                    .orElseThrow(() ->
                            new NoSuchElementException(
                                    "Producto no encontrado"
                            )
                    );

            if (!Boolean.TRUE.equals(producto.getActivo())) {
                throw new IllegalArgumentException(
                        "Producto no disponible: " +
                                producto.getNombre()
                );
            }

            if (item.getCantidad() == null ||
                    item.getCantidad() <= 0) {
                throw new IllegalArgumentException(
                        "Cantidad inválida para " +
                                producto.getNombre()
                );
            }

            if (producto.getStock() < item.getCantidad()) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para " +
                                producto.getNombre()
                );
            }

            BigDecimal subtotal = producto.getPrecio()
                    .multiply(
                            BigDecimal.valueOf(item.getCantidad())
                    );

            DetallePedido detalle = DetallePedido.builder()
                    .producto(producto)
                    .cantidad(item.getCantidad())
                    .precioUnitario(producto.getPrecio())
                    .subtotal(subtotal)
                    .build();

            pedido.agregarDetalle(detalle);

            producto.setStock(
                    producto.getStock() - item.getCantidad()
            );

            total = total.add(subtotal);
        }

        pedido.setTotal(total);

        Pedido pedidoGuardado = pedidoRepository.saveAndFlush(pedido);

        carrito.vaciar();
        carritoRepository.saveAndFlush(carrito);

        return pedidoGuardado;
    }

    @Override
    @Transactional
    public Pedido handle(UpdatePedidoEstadoCommand command) {

        if (command == null || command.pedidoId() == null) {
            throw new IllegalArgumentException(
                    "El ID del pedido es obligatorio"
            );
        }

        if (command.estado() == null) {
            throw new IllegalArgumentException(
                    "El estado del pedido es obligatorio"
            );
        }

        Pedido pedido = pedidoRepository.findById(command.pedidoId())
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Pedido no encontrado"
                        )
                );

        if (pedido.getEstado() == EstadoPedido.CANCELADO ||
                pedido.getEstado() == EstadoPedido.ENTREGADO) {

            throw new IllegalArgumentException(
                    "No se puede modificar un pedido finalizado"
            );
        }

        pedido.setEstado(command.estado());

        return pedidoRepository.save(pedido);
    }
}
