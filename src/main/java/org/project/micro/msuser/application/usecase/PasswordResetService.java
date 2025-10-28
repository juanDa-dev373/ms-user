package org.project.micro.msuser.application.usecase;

import lombok.RequiredArgsConstructor;
import org.project.micro.msuser.application.dto.PasswordChanged;
import org.project.micro.msuser.application.dto.PasswordResetRequested;
import org.project.micro.msuser.application.dto.TokenDto;
import org.project.micro.msuser.domain.reset.PasswordResetToken;
import org.project.micro.msuser.domain.reset.gateway.PasswordResetTokenGateway;
import org.project.micro.msuser.domain.user.User;
import org.project.micro.msuser.domain.user.gateway.PublishGateway;
import org.project.micro.msuser.domain.user.gateway.UserGateway;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserGateway userRepo;
    private final PasswordResetTokenGateway tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final PublishGateway notificationPublisher;


    public Mono<TokenDto> startReset(String username) {
        return userRepo.findByUsername(username)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado")))
                .flatMap(u -> {
                    String token = UUID.randomUUID().toString().replaceAll("-", "");
                    Instant expiresAt = Instant.now().plus(15, ChronoUnit.MINUTES);
                    PasswordResetToken prt = PasswordResetToken.builder()
                            .token(token)
                            .userId(u.getId())
                            .expiresAt(expiresAt)
                            .used(false)
                            .build();
                    String ruta = "/users/" + u.getId() + "/password";
                    return tokenRepo.save(prt)
                            .doOnSuccess(saved -> publishMessage(u, ruta, expiresAt)) // ✅ dentro del flujo
                            .thenReturn(new TokenDto(token, ruta));
                });
    }
    private void publishMessage(User user, String ruta, Instant expiresAt){
        notificationPublisher.publishPasswordResetRequested(
                PasswordResetRequested.builder()
                        .email(user.getUsername())
                        .name(String.format("%s %s",user.getFirstname(),user.getLastname()))
                        .occurredAt(Instant.now())
                        .userId(String.valueOf(user.getId()))
                        .resetUrl(ruta)
                        .expiresInMin(expiresAt.getNano())
                        .build()
        );
    }

    public Mono<Void> confirmReset(String token, String newPassword) {
        return tokenRepo.findByToken(token)
                .switchIfEmpty(Mono.error(new ResponseStatusException(BAD_REQUEST, "Token invalido")))
                .flatMap(t -> {
                    if (t.isUsed() || t.getExpiresAt().isBefore(Instant.now())) {
                        throw new ResponseStatusException(BAD_REQUEST, "Token expirado o usado");
                    }
                    t.setUsed(true);
                    return userRepo.findById(t.getUserId())
                            .flatMap(u -> {
                                u.setPassword(passwordEncoder.encode(newPassword));
                                return tokenRepo.save(t)
                                        .then(userRepo.save(u)
                                                .doOnSuccess(this::publishPasswordReset)
                                                .then());
                            });

                });
    }
    public void publishPasswordReset(User user){
        notificationPublisher.publishPasswordChanged(
                PasswordChanged.builder()
                        .email(user.getUsername())
                        .name(String.format("%s %s",user.getFirstname(),user.getLastname()))
                        .occurredAt(Instant.now())
                        .userId(String.valueOf(user.getId()))
                        .build()
        );
    }

    /**public void confirmReset(String token, String newPassword) {
        // Busca el token que se crea cuando se consume el servicio de forgot-password lo cual no genera
        // un token de jwt si no que propio del sistema
        PasswordResetToken prt = tokenRepo.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Token inválido"));

        if (prt.isUsed() || prt.getExpiresAt().isBefore(Instant.now())) {
            throw new ResponseStatusException(BAD_REQUEST, "Token expirado o usado");
        }
        User user = prt.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        prt.setUsed(true);
        tokenRepo.save(prt); // persistimos used=true
        userRepo.save(user);

        //*********************
        notificationPublisher.publishPasswordChanged(
                PasswordChanged.builder()
                        .email(user.getUsername())
                        .name(String.format("%s %s",user.getFirstname(),user.getLastname()))
                        .occurredAt(Instant.now())
                        .userId(String.valueOf(user.getId()))
                        .build()
        );
        //***********************
    }**/
}
