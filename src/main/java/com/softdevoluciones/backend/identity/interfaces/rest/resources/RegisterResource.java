package com.softdevoluciones.backend.identity.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterResource(
        @NotBlank String nombre,
        @NotBlank @Email String email,
        @NotBlank String password
) {}