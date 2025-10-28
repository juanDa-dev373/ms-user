package org.project.micro.msuser.infrastructure.entry_points.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.project.micro.msuser.application.dto.ForgotPasswordRequest;
import org.project.micro.msuser.application.dto.TokenDto;
import org.project.micro.msuser.application.usecase.PasswordResetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/tokens-recovery")
@RequiredArgsConstructor
public class TokenController {


    private final PasswordResetService passwordResetService;

    // Reto 3: iniciar recuperación
    @Operation(summary = "Olvido su contraseña, iniciar recuperacion", security = @SecurityRequirement(name = ""))
    @PostMapping()
    public ResponseEntity<Mono<TokenDto>> forgot(@RequestBody ForgotPasswordRequest req) {
        return ResponseEntity.ok(passwordResetService.startReset(req.username()));
    }
}
