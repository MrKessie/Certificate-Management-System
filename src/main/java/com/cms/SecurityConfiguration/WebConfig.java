package com.cms.SecurityConfiguration;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/styles/**", "/static/scripts/**", "/static/images/**")
                .addResourceLocations("classpath:/static/styles/", "classpath:/static/scripts/", "classpath:/static/images/");
    }
}
