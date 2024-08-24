package com.cms.SecurityConfiguration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/styles/**", "/static/scripts/**", "/static/images/**")
                .addResourceLocations("classpath:/static/styles/", "classpath:/static/scripts/", "classpath:/static/images/");
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/certificates/**")
                .allowedOrigins("http://localhost:8080")  // Allow your application origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
