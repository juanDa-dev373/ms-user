package org.project.micro.msuser.application.dto;

import lombok.Builder;

@Builder
public record EventDTO(
        Long idNotification,
        String emailUser,
        String channelNotification
) {
}
