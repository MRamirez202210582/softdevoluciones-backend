
package com.softdevoluciones.backend.ordering.domain.model.commands;

public record CreatePedidoCommand(
        Long usuarioId,
        String clienteNombre,
        String celular,
        String direccion
) {}
