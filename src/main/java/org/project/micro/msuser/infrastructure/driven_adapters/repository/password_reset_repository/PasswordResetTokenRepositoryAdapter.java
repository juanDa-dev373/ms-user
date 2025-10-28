package org.project.micro.msuser.infrastructure.driven_adapters.repository.password_reset_repository;

import org.project.micro.msuser.domain.reset.PasswordResetToken;
import org.project.micro.msuser.domain.reset.gateway.PasswordResetTokenGateway;
import org.project.micro.msuser.infrastructure.driven_adapters.helpers.ReactiveAdapterOperations;
import org.project.micro.msuser.infrastructure.driven_adapters.repository.password_reset_repository.entities.PasswordResetTokenEntity;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class PasswordResetTokenRepositoryAdapter extends ReactiveAdapterOperations<PasswordResetToken, PasswordResetTokenEntity, Long, PasswordResetTokenRepository>
        implements PasswordResetTokenGateway {

    public PasswordResetTokenRepositoryAdapter(PasswordResetTokenRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, PasswordResetToken.class));
    }

    @Override
    public Mono<PasswordResetToken> findByToken(String token) {
        return repository.findByToken(token);
    }
}
