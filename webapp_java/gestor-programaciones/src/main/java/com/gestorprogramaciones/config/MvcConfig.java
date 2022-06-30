package com.gestorprogramaciones.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        
        registry.addViewController("/").setViewName("app_login");
        registry.addViewController("/app_login").setViewName("app_login");
        registry.addViewController("/new_user").setViewName("new_user");
        registry.addViewController("/diario").setViewName("diario");
        registry.addViewController("/modulos").setViewName("modulos");
        registry.addViewController("/programacion").setViewName("programacion");
        registry.addViewController("/evaluacion").setViewName("evaluacion");
    }

}
