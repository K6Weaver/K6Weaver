package com.k6.weaver.config;

import com.k6.weaver.Controller.K6ScriptController;
import com.k6.weaver.Service.K6WeaverService;
import com.k6.weaver.generator.ControllerScanner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class K6WeaverAutoConfiguration {
    @Bean
    public ControllerScanner controllerScanner(ApplicationContext applicationContext) {
        return new ControllerScanner(applicationContext);
    }

    @Bean
    public K6WeaverService k6WeaverService() {
        return new K6WeaverService();
    }

    @Bean
    public K6ScriptController k6ScriptController(K6WeaverService k6WeaverService) {
        return new K6ScriptController(k6WeaverService);
    }
}
