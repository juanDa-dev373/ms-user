package org.project.micro.msuser.infrastructure.entry_points.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.micro.msuser.application.dto.RegisterRequest;
import org.project.micro.msuser.application.dto.ResetPasswordRequest;
import org.project.micro.msuser.application.dto.UserDto;
import org.project.micro.msuser.application.dto.UserUpdateRequest;
import org.project.micro.msuser.application.usecase.AuthService;
import org.project.micro.msuser.application.usecase.PasswordResetService;
import org.project.micro.msuser.application.usecase.UserUsecase;
import org.project.micro.msuser.domain.enums.Role;
import org.project.micro.msuser.domain.user.User;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsersControllerTest {

    @Mock
    private UserUsecase userService;

    @Mock
    private AuthService authService;

    @Mock
    private PasswordResetService passwordResetService;

    @InjectMocks
    private UsersController controller;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("juan");
        mockUser.setFirstname("Juan");
        mockUser.setLastname("Pérez");
        mockUser.setCountry("CL");
        mockUser.setPassword("1234");
        mockUser.setRole(Role.USER);
    }

    @Test
    void list_shouldReturnFluxOfUsers() {
        when(userService.search("", 10, 0)).thenReturn(Flux.just(mockUser));

        StepVerifier.create(controller.list("", 10, 0))
                .expectNext(mockUser)
                .verifyComplete();
    }

    @Test
    void update_shouldReturnUpdatedUser() {
        UserUpdateRequest req = new UserUpdateRequest("Nuevo", "Apellido", "PE", null);
        User updated = User.builder()
                .id(1L)
                .username("juan")
                .firstname("Nuevo")
                .lastname("Apellido")
                .country("PE")
                .role(Role.USER)
                .password("1234")
                .build();

        when(userService.update(eq(1L), any(UserUpdateRequest.class)))
                .thenReturn(Mono.just(updated));

        StepVerifier.create(controller.update(1L, req))
                .expectNextMatches(u -> u.getFirstname().equals("Nuevo"))
                .verifyComplete();
    }

    @Test
    void patch_shouldReturnPatchedUser() {
        UserUpdateRequest req = new UserUpdateRequest("Juan", "Pérez", "AR", "newPass");
        User patched = User.builder()
                .id(1L)
                .username("juan")
                .firstname("Juan")
                .lastname("Pérez")
                .country("AR")
                .role(Role.USER)
                .password("newPass")
                .build();

        when(userService.patch(eq(1L), any(UserUpdateRequest.class)))
                .thenReturn(Mono.just(patched));

        StepVerifier.create(controller.patch(1L, req))
                .expectNextMatches(u -> u.getCountry().equals("AR"))
                .verifyComplete();
    }

    @Test
    void delete_shouldReturnEmptyMono() {
        when(userService.delete(eq(1L))).thenReturn(Mono.empty());

        StepVerifier.create(controller.delete(1L))
                .verifyComplete();
    }

    @Test
    void register_shouldReturnUser() {
        RegisterRequest request = new RegisterRequest("juan", "1234", "Juan", "Pérez", "CL");
        when(authService.register(any(RegisterRequest.class))).thenReturn(Mono.just(mockUser));

        StepVerifier.create(controller.register(request))
                .expectNext(mockUser)
                .verifyComplete();
    }

    @Test
    void reset_shouldReturnEmptyMono() {
        ResetPasswordRequest req = new ResetPasswordRequest("token123", "nuevaClave");
        when(passwordResetService.confirmReset(eq("token123"), eq("nuevaClave")))
                .thenReturn(Mono.empty());

        StepVerifier.create(controller.reset("1", req))
                .verifyComplete();
    }

    @Test
    void me_shouldReturnResponseEntity() {
        ResponseEntity<?> response = controller.me(mockUser);
        assertEquals("juan", ((UserDto) response.getBody()).username());
        assertEquals(200, response.getStatusCode().value());
    }
}
