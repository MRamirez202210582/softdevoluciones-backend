
package com.softdevoluciones.backend.returns.application.internal.commandservices;

import com.softdevoluciones.backend.returns.domain.model.aggregates.SolicitudDevolucion;
import com.softdevoluciones.backend.returns.domain.model.commands.CreateDevolucionCommand;
import com.softdevoluciones.backend.returns.domain.model.commands.UpdateDevolucionEstadoCommand;
import com.softdevoluciones.backend.returns.domain.model.valueobjects.EstadoDevolucion;
import com.softdevoluciones.backend.returns.domain.services.CompraConsultaService;
import com.softdevoluciones.backend.returns.domain.services.DevolucionCommandService;
import com.softdevoluciones.backend.returns.infrastructure.persistence.jpa.repositories.SolicitudDevolucionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

@Service
public class DevolucionCommandServiceImpl
        implements DevolucionCommandService {

    private final SolicitudDevolucionRepository repository;
    private final CompraConsultaService compraConsultaService;

    public DevolucionCommandServiceImpl(
            SolicitudDevolucionRepository repository,
            CompraConsultaService compraConsultaService
    ) {
        this.repository = repository;
        this.compraConsultaService = compraConsultaService;
    }

    @Override
    @Transactional
    public SolicitudDevolucion crearDevolucion(
            CreateDevolucionCommand command,
            Long usuarioId
    ) {
        if (command == null || command.compraId() == null
                || usuarioId == null || command.motivo() == null) {
            throw new IllegalArgumentException(
                    "Los datos de la solicitud son obligatorios."
            );
        }

        if (command.detalles() == null
                || command.detalles().isEmpty()) {
            throw new IllegalArgumentException(
                    "Debe seleccionar al menos un producto."
            );
        }

        if (!compraConsultaService.perteneceAlUsuario(
                command.compraId(), usuarioId)) {
            throw new IllegalArgumentException(
                    "La compra no pertenece al usuario."
            );
        }

        SolicitudDevolucion solicitud = new SolicitudDevolucion(
                command.compraId(),
                usuarioId,
                command.motivo(),
                command.comentario()
        );

        Set<Long> detallesProcesados = new HashSet<>();

        for (var item : command.detalles()) {

            if (item == null || item.detalleCompraId() == null
                    || item.cantidad() == null
                    || item.cantidad() <= 0) {
                throw new IllegalArgumentException(
                        "El detalle y su cantidad deben ser válidos."
                );
            }

            if (!detallesProcesados.add(item.detalleCompraId())) {
                throw new IllegalArgumentException(
                        "No puede repetir un detalle de compra."
                );
            }

            var detalleCompra = compraConsultaService.obtenerDetalle(
                    command.compraId(),
                    item.detalleCompraId()
            );

            if (detalleCompra == null) {
                throw new IllegalArgumentException(
                        "El producto no pertenece a la compra."
                );
            }

            Long cantidadReservada = repository.sumarCantidadDevuelta(
                    command.compraId(),
                    item.detalleCompraId(),
                    EnumSet.of(
                            EstadoDevolucion.SOLICITADA,
                            EstadoDevolucion.EN_REVISION,
                            EstadoDevolucion.APROBADA,
                            EstadoDevolucion.COMPLETADA
                    )
            );

            long cantidadDisponible =
                    detalleCompra.cantidadComprada()
                            - cantidadReservada;

            if (item.cantidad() > cantidadDisponible) {
                throw new IllegalArgumentException(
                        "La cantidad solicitada supera las unidades disponibles."
                );
            }

            solicitud.agregarDetalle(
                    item.detalleCompraId(),
                    item.cantidad(),
                    detalleCompra.precioUnitario()
            );
        }

        return repository.save(solicitud);
    }

    @Override
    @Transactional
    public SolicitudDevolucion actualizarEstado(
            UpdateDevolucionEstadoCommand command
    ) {
        if (command == null || command.devolucionId() == null) {
            throw new IllegalArgumentException(
                    "Debe indicar la devolución."
            );
        }

        SolicitudDevolucion solicitud = repository.findById(
                command.devolucionId()
        ).orElseThrow(() ->
                new IllegalArgumentException(
                        "La solicitud de devolución no existe."
                )
        );

        solicitud.cambiarEstado(
                command.nuevoEstado(),
                command.observacionOperador()
        );

        return repository.save(solicitud);
    }
}
