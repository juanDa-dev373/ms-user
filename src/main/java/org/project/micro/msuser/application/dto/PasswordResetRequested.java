package org.project.micro.msuser.application.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.Map;

@Builder
public record PasswordResetRequested(
        String eventId,
        Instant occurredAt,
        String userId,
        String email,
        String name,
        String resetUrl,
        Integer expiresInMin,
        Map<String, Object> meta
) {
}
