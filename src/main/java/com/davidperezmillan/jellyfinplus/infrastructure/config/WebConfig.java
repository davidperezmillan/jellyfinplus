package com.davidperezmillan.jellyfinplus.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración global de CORS para permitir peticiones desde el front.
 * Ajusta allowedOrigins según sea necesario; por seguridad es preferible usar el origen exacto en lugar de '*'.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://192.168.68.195:80",
                        "https://192.168.68.195:443",
                        "http://192.168.68.195:85",
                        "http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
