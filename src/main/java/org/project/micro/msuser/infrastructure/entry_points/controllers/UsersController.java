package org.project.micro.msuser.infrastructure.entry_points.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.micro.msuser.application.dto.*;
import org.project.micro.msuser.application.usecase.AuthService;
import org.project.micro.msuser.application.usecase.PasswordResetService;
import org.project.micro.msuser.application.usecase.UserUsecase;
import org.project.micro.msuser.domain.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

    private final UserUsecase userService;
    private final AuthService authService;
    private final PasswordResetService passwordResetService;


    private static UserDto toDto(User u) {
        return new UserDto(u.getId(), u.getUsername(), u.getFirstname(), u.getLastname(), u.getCountry(), u.getRole());
    }

    // Reto 4: listado con paginación (SIN admin: cualquier autenticado puede listar)
    @Operation(summary = "Listar usuarios (paginado)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "No autenticado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Message.class)))
    @GetMapping("/")
    public Flux<User> list(@RequestParam(defaultValue = "") String q,
                           @RequestParam(defaultValue = "10") int limit,
                           @RequestParam(defaultValue = "0") int offset) {
        return userService.search(q, limit, offset);
    }

    @Operation(summary = "Mi perfil", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "No autenticado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Message.class)))
    @GetMapping("/me")
    public ResponseEntity<UserDto> me(@AuthenticationPrincipal User me) {
        return ResponseEntity.ok(toDto(me));
    }

    @Operation(summary = "Actualizar usuario (reemplazo)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Actualizado")
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Message.class)))
    @ApiResponse(responseCode = "401", description = "No autenticado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Message.class)))
    @ApiResponse(responseCode = "403", description = "Prohibido",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Message.class)))
    @ApiResponse(responseCode = "404", description = "No encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Message.class)))
    @ApiResponse(responseCode = "409", description = "Conflict",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Message.class)))
    @PutMapping("/{id}")
    public Mono<User> update(@Valid @PathVariable Long id, @Valid @RequestBody UserUpdateRequest req) {
        return userService.update(id, req);
    }

    @Operation(summary = "Actualizar usuario (parcial)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Actualizado")
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Message.class)))
    @ApiResponse(responseCode = "401", description = "No autenticado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Message.class)))
    @ApiResponse(responseCode = "403", description = "Prohibido",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Message.class)))
    @ApiResponse(responseCode = "404", description = "No encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Message.class)))
    @ApiResponse(responseCode = "409", description = "Conflict",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Message.class)))
    @PatchMapping("/{id}")
    public Mono<User> patch(@Valid @PathVariable Long id, @Valid @RequestBody UserUpdateRequest req) {
        return userService.patch(id, req);
    }

    // Eliminar (solo propietario se puede eliminar)
    @Operation(summary = "Eliminar usuario", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "Eliminado")
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Message.class)))
    @ApiResponse(responseCode = "401", description = "No autenticado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Message.class)))
    @ApiResponse(responseCode = "403", description = "Prohibido",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Message.class)))
    @ApiResponse(responseCode = "404", description = "No encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Message.class)))
    @DeleteMapping("/{id}")
    public Mono<Void> delete(@Valid @PathVariable Long id) {
        return userService.delete(id);
    }

    @Operation(summary = "Crear Usuario", security = @SecurityRequirement(name = ""))
    @ApiResponse(responseCode = "200", description = "Creado")
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Message.class)))
    @ApiResponse(responseCode = "409", description = "Conflict",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Message.class)))
    @PostMapping
    public Mono<User> register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @Operation(summary = "Olvido su contraseña, Confirme su cambio", security = @SecurityRequirement(name = ""))
    @PatchMapping("/password/{id}")
    public Mono<Void> reset(@PathVariable(value = "id") String id, @RequestBody ResetPasswordRequest req) {
        return passwordResetService.confirmReset(req.token(), req.newPassword());
    }
}
