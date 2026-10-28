
package com.softdevoluciones.backend.ordering.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreatePedidoResource(

        @NotBlank(message = "El nombre es obligatorio")
        String clienteNombre,

        @NotBlank(message = "El celular es obligatorio")
        @Pattern(
                regexp = "^[0-9]{9}$",
                message = "El celular debe tener 9 dígitos"
        )
        String celular,

        @NotBlank(message = "La dirección es obligatoria")
        @Size(max = 255)
        String direccion
) {}
