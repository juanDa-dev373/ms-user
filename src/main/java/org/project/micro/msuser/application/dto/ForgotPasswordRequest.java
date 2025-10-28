package org.project.micro.msuser.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ForgotPasswordRequest(
        @Valid
        @NotNull(message = "El campo Username no puede estar null")
        @NotEmpty(message = "El campo Username no debe estar vacio")
        String username
) {}
