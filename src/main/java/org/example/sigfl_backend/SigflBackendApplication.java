package org.example.sigfl_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SigflBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SigflBackendApplication.class, args);
    }

}
