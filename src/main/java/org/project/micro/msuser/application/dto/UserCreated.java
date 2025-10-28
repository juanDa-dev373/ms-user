package org.project.micro.msuser.application.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.Map;

@Builder
public record UserCreated(
        String eventId,
        Instant occurredAt,
        String userId,
        String email,
        String phone,
        String name,
        String confirmUrl,
        Map<String, Object> meta
) {
}
