
package com.softdevoluciones.backend.shopping.domain.model.aggregates;

import com.softdevoluciones.backend.catalog.domain.model.aggregates.Producto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "detalles_carrito",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"carrito_id", "producto_id"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;
}
