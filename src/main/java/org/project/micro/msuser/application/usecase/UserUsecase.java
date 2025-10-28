package org.project.micro.msuser.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.micro.msuser.application.dto.UserUpdateRequest;
import org.project.micro.msuser.domain.user.User;
import org.project.micro.msuser.domain.user.gateway.UserGateway;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.NOT_FOUND;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserUsecase {


    private final UserGateway userRepo;
    private final PasswordEncoder passwordEncoder;


    public Flux<User> search(String q, int limit, int offset) {
        log.info("Searching for users with sort {}", q);
        return userRepo.search(q, limit, offset);
    }
    public Mono<User> update(Long id, UserUpdateRequest req) {
        log.info("Actualizando usuario con id {}", id);
        return userRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(NOT_FOUND, "No existe")))
                .flatMap(u -> {
                    log.info("Setteando datos para el usuario {}", u.getUsername());
                    u.setFirstname(req.firstname());
                    u.setLastname(req.lastname());
                    u.setCountry(req.country());
                    return userRepo.save(u);
                })
                .doOnError(e -> log.error("Ocurrio un error: {}", e.getMessage(), e)) ;
    }

    public Mono<User> patch(Long id, UserUpdateRequest req){

        return userRepo.findById(id).flatMap(u->{
            u.setFirstname(req.firstname());
            u.setLastname(req.lastname());
            u.setCountry(req.country());
            u.setPassword(passwordEncoder.encode(req.newPassword()));
            return userRepo.save(u);
        }).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe")));
    }

    public Mono<Void> delete(Long id) {
        return userRepo.existsById(id)
                .flatMap( b ->{
                    if(Boolean.FALSE.equals(b)){
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe"));
                    }
                    return userRepo.deleteById(id);
                });
    }
}