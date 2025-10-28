package org.project.micro.msuser.domain.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Evento de negocio genérico del dominio.
 * Estructura esperada en JSON:
 * {
 *   "eventId": "e7b3f...",
 *   "type": "auth.login_detected",
 *   "occurredAt": "2025-09-28T15:22:05Z",
 *   "payload": { ... },   // variables del evento (userId, email, ip, etc.)
 *   "meta": { ... }       // traceId, productor, etc. (opcional)
 * }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record DomainEvent(
        String eventId,
        String type,
        Instant occurredAt,
        Map<String, Object> payload,
        Map<String, Object> meta
) {
    public DomainEvent {
        Objects.requireNonNull(type, "type es obligatorio");
        // Defaults seguros
        if (occurredAt == null) occurredAt = Instant.now();
        if (payload == null) payload = Map.of();
        if (meta == null) meta = Map.of();
    }

    /** Devuelve un valor String del payload si existe. */
    public Optional<String> payloadString(String key) {
        Object v = payload.get(key);
        return v == null ? Optional.empty() : Optional.of(String.valueOf(v));
    }

    /** Devuelve un valor tipado del payload si existe y hace cast seguro. */
    @SuppressWarnings("unchecked")
    public <T> Optional<T> payloadAs(String key, Class<T> type) {
        Object v = payload.get(key);
        return (v != null && type.isInstance(v)) ? Optional.of((T) v) : Optional.empty();
    }

    /** Convierte todo el payload a un tipo fuerte usando ObjectMapper.convertValue. */
    public <T> T payloadAs(Class<T> targetType, ObjectMapper mapper) {
        return mapper.convertValue(payload, targetType);
    }

    /** Fecha del evento en una zona horaria dada. */
    public ZonedDateTime occurredAt(ZoneId zoneId) {
        return ZonedDateTime.ofInstant(occurredAt, zoneId);
    }

    /**
     * Idempotency key recomendada:
     * 1) meta.idempotencyKey si viene definida
     * 2) eventId si viene definida
     * 3) fallback: type:userId:yyyyMMddHHmm (truncado a minutos)
     */
    public String idempotencyKey() {
        String metaKey = metaString("idempotencyKey").orElse(null);
        if (metaKey != null && !metaKey.isBlank()) return metaKey;

        if (eventId != null && !eventId.isBlank()) return eventId;

        String userId = payloadString("userId").orElse("na");
        String ts = occurredAt.truncatedTo(ChronoUnit.MINUTES).toString(); // ISO-8601
        return String.join(":", type, userId, ts);
    }

    /** Helper para leer meta como String. */
    public Optional<String> metaString(String key) {
        Object v = meta.get(key);
        return v == null ? Optional.empty() : Optional.of(String.valueOf(v));
    }

    /** Fábrica a partir de JSON crudo. */
    public static DomainEvent fromJson(String json, ObjectMapper mapper) {
        try {
            return mapper.readValue(json, DomainEvent.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("No se pudo parsear DomainEvent desde JSON", e);
        }
    }

    /** Fábrica conveniente. */
    public static DomainEvent of(String eventId, String type, Instant occurredAt,
                                 Map<String, Object> payload, Map<String, Object> meta) {
        return new DomainEvent(eventId, type, occurredAt, payload, meta);
    }
}

