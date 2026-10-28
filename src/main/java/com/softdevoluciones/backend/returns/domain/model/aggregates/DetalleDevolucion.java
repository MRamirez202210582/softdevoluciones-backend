
package com.softdevoluciones.backend.returns.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "detalles_devolucion")
@Getter
@NoArgsConstructor
public class DetalleDevolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "solicitud_id", nullable = false)
    private SolicitudDevolucion solicitud;

    @Column(name = "detalle_compra_id", nullable = false)
    private Long detalleCompraId;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false,
            precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal importe;

    public DetalleDevolucion(
            SolicitudDevolucion solicitud,
            Long detalleCompraId,
            Integer cantidad,
            BigDecimal precioUnitario
    ) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero."
            );
        }

        if (precioUnitario == null
                || precioUnitario.signum() < 0) {
            throw new IllegalArgumentException(
                    "El precio unitario no es válido."
            );
        }

        this.solicitud = solicitud;
        this.detalleCompraId = detalleCompraId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;

        this.importe = precioUnitario.multiply(
                BigDecimal.valueOf(cantidad)
        );
    }
}
