package org.project.micro.msuser;

import org.reactivecommons.async.impl.config.annotations.EnableDomainEventBus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDomainEventBus
@SpringBootApplication
public class MsUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsUserApplication.class, args);
    }

}
