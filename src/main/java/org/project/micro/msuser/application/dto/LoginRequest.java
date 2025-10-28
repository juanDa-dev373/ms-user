package org.project.micro.msuser.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @Valid
    @NotNull(message = "El campo Username no puede estar null")
    @NotEmpty(message = "El campo Username no debe estar vacio")
    String username;
    @Valid
    @NotNull(message = "El campo password no puede estar null")
    @NotEmpty(message = "El campo password no debe estar vacio")
    String password;
}
