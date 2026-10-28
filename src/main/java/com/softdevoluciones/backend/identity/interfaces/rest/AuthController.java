package com.softdevoluciones.backend.identity.interfaces.rest;

import com.softdevoluciones.backend.identity.domain.model.commands.RegisterCommand;
import com.softdevoluciones.backend.identity.domain.model.queries.GetUsuarioByEmailQuery;
import com.softdevoluciones.backend.identity.domain.services.UsuarioCommandService;
import com.softdevoluciones.backend.identity.domain.services.UsuarioQueryService;
import com.softdevoluciones.backend.identity.infrastructure.security.JwtService;
import com.softdevoluciones.backend.identity.interfaces.rest.resources.*;
import com.softdevoluciones.backend.identity.interfaces.rest.transform.UsuarioResourceFromEntityAssembler;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioCommandService usuarioCommandService;
    private final UsuarioQueryService usuarioQueryService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<UsuarioResource> register(
            @Valid @RequestBody RegisterResource resource
    ) {
        try {
            var command = new RegisterCommand(
                    resource.nombre(),
                    resource.email(),
                    resource.password()
            );

            var usuario = usuarioCommandService.handle(command);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(UsuarioResourceFromEntityAssembler
                            .toResourceFromEntity(usuario));

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginResource resource
    ) {
        String email = resource.email().trim().toLowerCase();

        var usuario = usuarioQueryService.handle(
                new GetUsuarioByEmailQuery(email)
        ).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Credenciales incorrectas"
        ));

        if (!passwordEncoder.matches(
                resource.password(),
                usuario.getPassword()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Credenciales incorrectas"
            );
        }

        String token = jwtService.generateToken(usuario);

        return ResponseEntity.ok(
                new LoginResponse(
                        token,
                        UsuarioResourceFromEntityAssembler
                                .toResourceFromEntity(usuario)
                )
        );
    }
}