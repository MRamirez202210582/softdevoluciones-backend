
package com.softdevoluciones.backend.returns.domain.model.aggregates;

import com.softdevoluciones.backend.returns.domain.model.valueobjects.EstadoDevolucion;
import com.softdevoluciones.backend.returns.domain.model.valueobjects.MotivoDevolucion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "solicitudes_devolucion")
@Getter
@NoArgsConstructor
public class SolicitudDevolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "compra_id", nullable = false)
    private Long compraId;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fechaSolicitud;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoDevolucion estado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private MotivoDevolucion motivo;

    @Column(length = 1000)
    private String comentario;

    @Column(name = "observacion_operador", length = 1000)
    private String observacionOperador;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal importe;

    @OneToMany(
            mappedBy = "solicitud",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DetalleDevolucion> detalles = new ArrayList<>();

    public SolicitudDevolucion(
            Long compraId,
            Long usuarioId,
            MotivoDevolucion motivo,
            String comentario
    ) {
        this.compraId = compraId;
        this.usuarioId = usuarioId;
        this.motivo = motivo;
        this.comentario = comentario;
        this.fechaSolicitud = LocalDateTime.now();
        this.estado = EstadoDevolucion.SOLICITADA;
        this.importe = BigDecimal.ZERO;
    }

    public void agregarDetalle(
            Long detalleCompraId,
            Integer cantidad,
            BigDecimal precioUnitario
    ) {
        DetalleDevolucion detalle = new DetalleDevolucion(
                this,
                detalleCompraId,
                cantidad,
                precioUnitario
        );

        detalles.add(detalle);
        recalcularImporte();
    }

    private void recalcularImporte() {
        this.importe = detalles.stream()
                .map(DetalleDevolucion::getImporte)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void cambiarEstado(
            EstadoDevolucion nuevoEstado,
            String observacion
    ) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException(
                    "El nuevo estado es obligatorio."
            );
        }

        boolean transicionValida = switch (this.estado) {
            case SOLICITADA ->
                    nuevoEstado == EstadoDevolucion.EN_REVISION;

            case EN_REVISION ->
                    nuevoEstado == EstadoDevolucion.APROBADA
                            || nuevoEstado == EstadoDevolucion.RECHAZADA;

            case APROBADA ->
                    nuevoEstado == EstadoDevolucion.COMPLETADA;

            case RECHAZADA, COMPLETADA -> false;
        };

        if (!transicionValida) {
            throw new IllegalStateException(
                    "No se permite cambiar de "
                            + this.estado + " a " + nuevoEstado
            );
        }

        if (nuevoEstado == EstadoDevolucion.RECHAZADA
                && (observacion == null || observacion.isBlank())) {
            throw new IllegalArgumentException(
                    "Debe indicar una observación al rechazar."
            );
        }

        this.estado = nuevoEstado;
        this.observacionOperador = observacion;
    }
}
