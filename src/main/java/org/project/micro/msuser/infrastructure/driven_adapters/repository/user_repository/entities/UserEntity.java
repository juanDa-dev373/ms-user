package org.project.micro.msuser.infrastructure.driven_adapters.repository.user_repository.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.project.micro.msuser.domain.enums.Role;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("users")
public class UserEntity {

    @Id
    private Long id;

    @Column("username")
    private String username;

    @Column("firstname")
    private String firstname;

    @Column("lastname")
    private String lastname;

    @Column("country")
    private String country;

    @Column("password")
    private String password;

    @Column("role")
    private Role role;

    @Column("account_non_expired")
    @Builder.Default private boolean accountNonExpired = true;

    @Column("account_non_locked")
    @Builder.Default private boolean accountNonLocked = true;

    @Column("credentials_non_expired")
    @Builder.Default private boolean credentialsNonExpired = true;

    @Column("enabled")
    @Builder.Default private boolean enabled = true;
}
