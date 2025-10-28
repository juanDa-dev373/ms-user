package org.project.micro.msuser.domain.user.gateway;

import org.project.micro.msuser.domain.user.User;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserGateway {

    Mono<User> save(User user);
    Flux<User> search(String q, int limit, int offset);
    Mono<Void> deleteById(Long id);
    Mono<User> findById(Long id);
    Mono<Boolean> existsById(Long id);
    Mono<User> findByUsername(String username);
    Mono<Long> findLastId();
}
