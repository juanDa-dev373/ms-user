package org.project.micro.msuser.application.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.micro.msuser.application.dto.UserUpdateRequest;
import org.project.micro.msuser.domain.user.User;
import org.project.micro.msuser.domain.user.gateway.UserGateway;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class UserUsecaseTest {

    @Mock
    private UserGateway userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserUsecase usecase;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("juan");
        user.setFirstname("Juan");
        user.setLastname("Pérez");
        user.setCountry("CL");
        user.setPassword("1234");
    }

    @Test
    void search_returnsFluxOfUsers() {
        when(userRepo.search(anyString(), anyInt(), anyInt()))
                .thenReturn(Flux.just(user));

        StepVerifier.create(usecase.search("juan", 10, 0))
                .expectNext(user)
                .verifyComplete();

        verify(userRepo).search("juan", 10, 0);
    }

    @Test
    void update_existingUser_updatesAndSaves() {
        UserUpdateRequest req = new UserUpdateRequest("Juanito", "Gómez", "AR", null);

        when(userRepo.findById(1L)).thenReturn(Mono.just(user));
        when(userRepo.save(any(User.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(usecase.update(1L, req))
                .expectNextMatches(u ->
                        u.getFirstname().equals("Juanito")
                                && u.getLastname().equals("Gómez")
                                && u.getCountry().equals("AR"))
                .verifyComplete();

        verify(userRepo).save(any(User.class));
    }

    @Test
    void update_userNotFound_throwsError() {
        when(userRepo.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(usecase.update(1L, new UserUpdateRequest("a", "b", "c", null)))
                .expectErrorSatisfies(e -> {
                    assert e instanceof ResponseStatusException;
                    assert ((ResponseStatusException) e).getStatusCode().equals(NOT_FOUND);
                })
                .verify();
    }

    @Test
    void patch_updatesPasswordAndFields() {
        when(userRepo.findById(1L)).thenReturn(Mono.just(user));
        when(passwordEncoder.encode(anyString())).thenReturn("encoded123");
        when(userRepo.save(any(User.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        UserUpdateRequest req = new UserUpdateRequest("Nuevo", "Apellido", "PE", "nuevaClave");

        StepVerifier.create(usecase.patch(1L, req))
                .expectNextMatches(u ->
                        u.getFirstname().equals("Nuevo")
                                && u.getLastname().equals("Apellido")
                                && u.getCountry().equals("PE")
                                && u.getPassword().equals("encoded123"))
                .verifyComplete();

        verify(passwordEncoder).encode("nuevaClave");
        verify(userRepo).save(any(User.class));
    }

    @Test
    void delete_existingUser_deletesSuccessfully() {
        when(userRepo.existsById(1L)).thenReturn(Mono.just(true));
        when(userRepo.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(usecase.delete(1L))
                .verifyComplete();

        verify(userRepo).deleteById(1L);
    }

    @Test
    void delete_userNotFound_throwsError() {
        when(userRepo.existsById(1L)).thenReturn(Mono.just(false));

        StepVerifier.create(usecase.delete(1L))
                .expectErrorSatisfies(e -> {
                    assert e instanceof ResponseStatusException;
                    assert ((ResponseStatusException) e).getStatusCode().equals(NOT_FOUND);
                })
                .verify();
    }
}
