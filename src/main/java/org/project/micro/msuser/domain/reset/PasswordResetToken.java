package org.project.micro.msuser.domain.reset;

import lombok.*;

import java.time.Instant;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetToken {
    private Long id;
    private String token;
    private Long userId;
    private Instant expiresAt;
    private boolean used;
}
