package org.project.micro.msuser.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserUpdateRequest(
        @Valid
        @NotNull(message = "El campo firstname no puede estar null")
        @NotEmpty(message = "El campo firstname no debe estar vacio")
        String firstname,
        @Valid
        @NotNull(message = "El campo lastname no puede estar null")
        @NotEmpty(message = "El campo lastname no debe estar vacio")
        String lastname,
        @Valid
        @NotNull(message = "El campo newPassword no puede estar null")
        @NotEmpty(message = "El campo newPassword no debe estar vacio")
        String country,
        @Valid
        @NotNull(message = "El campo newPassword no puede estar null")
        @NotEmpty(message = "El campo newPassword no debe estar vacio")
        String newPassword
) {}
