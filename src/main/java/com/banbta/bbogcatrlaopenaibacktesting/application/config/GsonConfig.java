package com.banbta.bbogcatrlaopenaibacktesting.application.config;

import com.banbta.bbogcatrlaopenaibacktesting.common.LocalDateGsonAdapter;
import com.banbta.bbogcatrlaopenaibacktesting.common.LocalDateTimeGsonAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class GsonConfig {

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateGsonAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeGsonAdapter())
                .create();
    }
}