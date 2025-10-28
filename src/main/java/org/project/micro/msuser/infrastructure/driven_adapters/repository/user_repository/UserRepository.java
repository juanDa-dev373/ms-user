package org.project.micro.msuser.infrastructure.driven_adapters.repository.user_repository;

import org.project.micro.msuser.infrastructure.driven_adapters.repository.user_repository.entities.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<UserEntity, Long>, ReactiveQueryByExampleExecutor<UserEntity> {

    @Query("""
        SELECT *
        FROM users
        WHERE (:q IS NULL OR :q = ''
            OR username LIKE CONCAT('%', :q, '%')
            OR firstname LIKE CONCAT('%', :q, '%')
            OR lastname LIKE CONCAT('%', :q, '%'))
        ORDER BY id DESC
        LIMIT :limit OFFSET :offset
    """)
    Flux<UserEntity> search(@Param("q") String q, @Param("limit") int limit,@Param("offset") int offset);

    @Query("SELECT id FROM users ORDER BY id DESC LIMIT 1")
    Mono<Long> findLastId();

    @Query("SELECT * FROM users u WHERE u.username = :username")
    Mono<UserEntity> findByUsername(@Param("username") String username);

}
