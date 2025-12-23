package com.davidperezmillan.jellyfinplus;

import com.davidperezmillan.jellyfinplus.infrastructure.config.JellyfinConfig;
import com.davidperezmillan.jellyfinplus.infrastructure.config.TransmissionConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JellyfinConfig.class, TransmissionConfig.class})
public class JellyfinplusApplication {

    public static void main(String[] args) {
        SpringApplication.run(JellyfinplusApplication.class, args);
    }

}
