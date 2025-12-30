package com.tuxoftware.ms_padron_unico.config;

import org.n52.jackson.datatype.jts.JtsModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    // Esto habilita que cuando la API devuelva un "Point",
    // se vea como un JSON estándar: { "type": "Point", "coordinates": [-96.12, 18.08] }
    @Bean
    public JtsModule jtsModule() {
        return new JtsModule();
    }
}