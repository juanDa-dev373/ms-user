package org.project.micro.msuser.application.config;

import org.project.micro.msuser.domain.user.UserPrincipal;
import org.project.micro.msuser.domain.user.gateway.UserGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;


@Configuration
public class ApplicationConfig {

    @Bean
    public ReactiveUserDetailsService userDetailsService(UserGateway userRepository) {
        return username -> userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found: " + username)))
                .map(UserPrincipal::new);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
