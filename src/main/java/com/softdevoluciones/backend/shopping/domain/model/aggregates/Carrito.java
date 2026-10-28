package com.softdevoluciones.backend.shopping.domain.model.aggregates;

import com.softdevoluciones.backend.identity.domain.model.aggregate.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carritos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @OneToMany(
            mappedBy = "carrito",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<DetalleCarrito> detalles = new ArrayList<>();

    public void agregarDetalle(DetalleCarrito detalle) {
        detalle.setCarrito(this);
        detalles.add(detalle);
    }

    public void eliminarDetalle(DetalleCarrito detalle) {
        detalles.remove(detalle);
        detalle.setCarrito(null);
    }

    public void vaciar() {
        for (DetalleCarrito detalle : detalles) {
            detalle.setCarrito(null);
        }
        detalles.clear();
    }
}
