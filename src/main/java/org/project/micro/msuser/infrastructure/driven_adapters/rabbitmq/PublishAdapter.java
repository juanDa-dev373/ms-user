package org.project.micro.msuser.infrastructure.driven_adapters.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.project.micro.msuser.application.dto.AuthLoginDetected;
import org.project.micro.msuser.application.dto.PasswordChanged;
import org.project.micro.msuser.application.dto.PasswordResetRequested;
import org.project.micro.msuser.application.dto.UserCreated;
import org.project.micro.msuser.domain.user.gateway.PublishGateway;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.api.domain.DomainEventBus;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class PublishAdapter implements PublishGateway {

    private final DomainEventBus eventBus;

    @Override
    public Mono<Void> publishLoginDetected(AuthLoginDetected payload) {
        DomainEvent<AuthLoginDetected> event = new DomainEvent<>("auth.login_detected", payload.userId(), payload);
        return Mono.from(eventBus.emit(event)).then();
    }

    @Override
    public Mono<Void> publishUserCreated(UserCreated payload) {
        DomainEvent<UserCreated> event = new DomainEvent<>("user.created", payload.userId(), payload);
        return Mono.from(eventBus.emit(event)).then();
    }

    @Override
    public Mono<Void> publishPasswordResetRequested(PasswordResetRequested payload) {
        DomainEvent<PasswordResetRequested> event = new DomainEvent<>("auth.password_reset.requested", payload.userId(), payload);
        return Mono.from(eventBus.emit(event)).then();
    }

    @Override
    public Mono<Void> publishPasswordChanged(PasswordChanged payload) {
        DomainEvent<PasswordChanged> event = new DomainEvent<>("auth.password.changed", payload.userId(), payload);
        return Mono.from(eventBus.emit(event)).then();
    }
}
