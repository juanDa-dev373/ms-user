package org.project.micro.msuser.infrastructure.driven_adapters.repository.password_reset_repository.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("password_reset_token")
public class PasswordResetTokenEntity {

    @Id
    private Long id;

    @Column("token")
    private String token;

    @Column("user_id")
    private Long userId;

    @Column("expires_at")
    private Instant expiresAt;

    @Column("used")
    private boolean used;
}
