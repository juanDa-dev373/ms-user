package org.project.micro.msuser.application.dto;


import org.project.micro.msuser.domain.enums.Role;

public record UserDto(
        Long id,
        String username,
        String firstname,
        String lastname,
        String country,
        Role role
) {}
