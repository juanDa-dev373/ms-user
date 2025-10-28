package org.project.micro.msuser.infrastructure.entry_points.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.project.micro.msuser.application.dto.AuthResponse;
import org.project.micro.msuser.application.dto.LoginRequest;
import org.project.micro.msuser.application.usecase.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Inicie sesión", security = @SecurityRequirement(name = ""))
    @PostMapping("login")
    public ResponseEntity<Mono<AuthResponse>> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

}
