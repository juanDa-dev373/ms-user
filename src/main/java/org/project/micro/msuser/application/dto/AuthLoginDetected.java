package org.project.micro.msuser.application.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record AuthLoginDetected(
        Instant occurredAt,
        String userId,
        String email
) { }
