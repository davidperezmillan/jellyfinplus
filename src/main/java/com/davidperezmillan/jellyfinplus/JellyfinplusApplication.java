package com.davidperezmillan.jellyfinplus;

import com.davidperezmillan.jellyfinplus.infrastructure.config.JellyfinConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JellyfinConfig.class)
public class JellyfinplusApplication {

    public static void main(String[] args) {
        SpringApplication.run(JellyfinplusApplication.class, args);
    }

}
