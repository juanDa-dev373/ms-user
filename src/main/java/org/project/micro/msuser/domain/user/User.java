package org.project.micro.msuser.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.project.micro.msuser.domain.enums.Role;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long id;

    private String username;

    private String firstname;
    private String lastname;
    private String country;

    @JsonIgnore
    private String password;

    private Role role;

    @Builder.Default private boolean accountNonExpired = true;
    @Builder.Default private boolean accountNonLocked = true;
    @Builder.Default private boolean credentialsNonExpired = true;
    @Builder.Default private boolean enabled = true;
}
