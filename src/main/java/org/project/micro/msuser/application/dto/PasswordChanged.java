package org.project.micro.msuser.application.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.Map;

@Builder
public record PasswordChanged(
        String eventId,
        Instant occurredAt,
        String userId,
        String email,
        String phone,
        String name,
        Map<String, Object> meta
) { }
