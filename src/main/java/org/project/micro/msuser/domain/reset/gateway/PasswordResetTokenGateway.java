package org.project.micro.msuser.domain.reset.gateway;

import org.project.micro.msuser.application.dto.TokenDto;
import org.project.micro.msuser.domain.reset.PasswordResetToken;
import reactor.core.publisher.Mono;

public interface PasswordResetTokenGateway {
    Mono<PasswordResetToken> save(PasswordResetToken passwordResetToken);
    Mono<PasswordResetToken> findByToken(String token);
}
