package org.project.micro.msuser.infrastructure.entry_points.controllers;

import lombok.RequiredArgsConstructor;
import org.project.micro.msuser.application.dto.AuthLoginDetected;
import org.project.micro.msuser.domain.user.gateway.PublishGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Instant;

@RestController
@RequestMapping("/test/rabbit")
@RequiredArgsConstructor
public class TestRabbitController {

    private final PublishGateway notificationPublisher;

    @PostMapping("/send")
    public Mono<ResponseEntity<String>> send(@RequestParam(defaultValue = "Hola Rabbit!") String msg) {
        return notificationPublisher.publishLoginDetected(
                AuthLoginDetected.builder()
                        .email("user.getUsername()")
                        .occurredAt(Instant.now())
                        .userId("user.getId")
                        .build()
        ).thenReturn(ResponseEntity.ok("Enviado: " + msg));

    }
}