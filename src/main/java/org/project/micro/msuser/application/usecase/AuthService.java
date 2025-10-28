package org.project.micro.msuser.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.micro.msuser.application.dto.*;
import org.project.micro.msuser.domain.enums.Role;
import org.project.micro.msuser.domain.user.User;
import org.project.micro.msuser.domain.user.gateway.JwtGateway;
import org.project.micro.msuser.domain.user.gateway.PublishGateway;
import org.project.micro.msuser.domain.user.gateway.UserGateway;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import javax.naming.AuthenticationException;
import java.time.Instant;
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserGateway userRepository;
    private final JwtGateway jwtService;
    private final PasswordEncoder passwordEncoder;
    private final PublishGateway notificationPublisher;

    public Mono<AuthResponse> login(LoginRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .flatMap(user -> {
                    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                        return Mono.error(new AuthenticationException("Credenciales inválidas"));
                    }
                    return jwtService.getToken(user)
                            .map(token -> {
                                notificationPublisher.publishLoginDetected(
                                        AuthLoginDetected.builder()
                                                .email(user.getUsername())
                                                .occurredAt(Instant.now())
                                                .userId(String.valueOf(user.getId()))
                                                .build()
                                );
                                return AuthResponse.builder()
                                        .token(token)
                                        .build();
                            });
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuario no encontrado")));
    }


    public Mono<User> register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .country(request.getCountry())
                .role(Role.USER)
                .build();
        return userRepository.save(user)
                .doOnSuccess(s ->{
                        log.info("Usuario agregado con username: {}", user.getUsername());
                        notificationPublisher.publishUserCreated(
                                UserCreated.builder()
                                        .email(user.getUsername())
                                        .occurredAt(Instant.now())
                                        .userId(String.valueOf(user.getId()))
                                        .confirmUrl("pendienteImplementar")
                                        .name(String.format("%s %s",user.getFirstname(),user.getLastname()))
                                        .build()
                        );}
                );
    }
}
