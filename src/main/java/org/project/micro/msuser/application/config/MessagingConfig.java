package org.project.micro.msuser.application.config;

import org.project.micro.msuser.domain.user.gateway.PublishGateway;
import org.project.micro.msuser.infrastructure.driven_adapters.rabbitmq.PublishAdapter;
import org.reactivecommons.api.domain.DomainEventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    @Bean
    public PublishGateway notificationPublisher(DomainEventBus eventBus) {
        return new PublishAdapter(eventBus);
    }

}
