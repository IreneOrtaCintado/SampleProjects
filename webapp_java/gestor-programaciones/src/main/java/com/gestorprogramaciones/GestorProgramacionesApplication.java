package com.gestorprogramaciones;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * MAIN
 * 
 */
@SpringBootApplication
public class GestorProgramacionesApplication {

    private static final Logger log = LoggerFactory.getLogger(GestorProgramacionesApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GestorProgramacionesApplication.class, args);
    }

}
