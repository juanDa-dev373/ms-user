package org.project.micro.msuser.domain.user.gateway;

import org.project.micro.msuser.application.dto.AuthLoginDetected;
import org.project.micro.msuser.application.dto.PasswordChanged;
import org.project.micro.msuser.application.dto.PasswordResetRequested;
import org.project.micro.msuser.application.dto.UserCreated;
import reactor.core.publisher.Mono;

public interface PublishGateway {
    Mono<Void> publishLoginDetected(AuthLoginDetected payload);
    Mono<Void> publishUserCreated(UserCreated payload) ;
    Mono<Void> publishPasswordResetRequested(PasswordResetRequested payload);
    Mono<Void> publishPasswordChanged(PasswordChanged payload);
}
