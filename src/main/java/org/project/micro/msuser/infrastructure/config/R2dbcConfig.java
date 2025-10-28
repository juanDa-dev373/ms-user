package org.project.micro.msuser.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories(basePackages = "org.project.micro.msuser.infrastructure.driven_adapters.repository")
public class R2dbcConfig {
}
