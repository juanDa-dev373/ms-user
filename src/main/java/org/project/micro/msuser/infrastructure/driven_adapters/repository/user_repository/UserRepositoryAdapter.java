package org.project.micro.msuser.infrastructure.driven_adapters.repository.user_repository;

import lombok.extern.slf4j.Slf4j;
import org.project.micro.msuser.domain.user.User;
import org.project.micro.msuser.domain.user.gateway.UserGateway;
import org.project.micro.msuser.infrastructure.driven_adapters.helpers.ReactiveAdapterOperations;
import org.project.micro.msuser.infrastructure.driven_adapters.repository.user_repository.entities.UserEntity;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class UserRepositoryAdapter extends ReactiveAdapterOperations<User, UserEntity, Long, UserRepository>
        implements UserGateway {

    public UserRepositoryAdapter(UserRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, User.class));
    }

    @Override
    public Flux<User> search(String q, int limit, int offset) {
        return repository.search(q, limit, offset).map(this::toEntity);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public Mono<User> findByUsername(String username) {
        return repository.findByUsername(username).map(this::toEntity);
    }

    @Override
    public Mono<Long> findLastId() {
        return repository.findLastId();
    }
}
