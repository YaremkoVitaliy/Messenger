package com.messenger.messengerservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.DefaultCorsProcessor;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {
    
    @Value("#{'${cors.allowedorigins}'.split(';')}")
    private List<String> allowedOrigins;

    @Value("#{'${cors.allowedmethods}'.split(';')}")
    private List<String> allowedMethods;

    @Value("#{'${cors.allowedheaders}'.split(';')}")
    private List<String> allowedHeaders;

    @Value("#{'${cors.exposedheaders}'.split(';')}")
    private List<String> exposedHeaders;
        
    @Bean
    CorsFilter corsFilter() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(allowedMethods);
        configuration.setAllowedHeaders(allowedHeaders);
        configuration.setExposedHeaders(exposedHeaders);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        CorsFilter filter = new CorsFilter(source);
        filter.setCorsProcessor(new DefaultCorsProcessor());

        return filter;
    }
}
