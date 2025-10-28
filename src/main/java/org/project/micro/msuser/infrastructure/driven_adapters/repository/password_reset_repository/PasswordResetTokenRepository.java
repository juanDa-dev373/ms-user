package org.project.micro.msuser.infrastructure.driven_adapters.repository.password_reset_repository;

import org.project.micro.msuser.domain.reset.PasswordResetToken;
import org.project.micro.msuser.infrastructure.driven_adapters.repository.password_reset_repository.entities.PasswordResetTokenEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PasswordResetTokenRepository extends ReactiveCrudRepository<PasswordResetTokenEntity, Long>, ReactiveQueryByExampleExecutor<PasswordResetTokenEntity> {

    @Query("""
        SELECT * 
        FROM password_reset_token 
        WHERE token = :token
    """)
    Mono<PasswordResetToken> findByToken(@Param("token") String token);

}
