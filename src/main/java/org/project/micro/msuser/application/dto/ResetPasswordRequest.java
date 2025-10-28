package org.project.micro.msuser.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ResetPasswordRequest(
        @Valid
        @NotNull(message = "El campo token no puede estar null")
        @NotEmpty(message = "El campo token no debe estar vacio")
        String token,
        @Valid
        @NotNull(message = "El campo newPassword no puede estar null")
        @NotEmpty(message = "El campo newPassword no debe estar vacio")
        String newPassword) {}
