package com.k6.weaver.config;

import com.k6.weaver.Controller.K6ScriptController;
import com.k6.weaver.Service.K6WeaverService;
import com.k6.weaver.generator.ControllerScanner;
import com.k6.weaver.util.K6WeaverConfigProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(K6WeaverConfigProperties.class)
public class K6WeaverAutoConfiguration {
    @Bean
    public ControllerScanner controllerScanner(ApplicationContext applicationContext, K6WeaverConfigProperties k6WeaverConfigProperties) {
        return new ControllerScanner(applicationContext, k6WeaverConfigProperties);
    }

    @Bean
    public K6WeaverService k6WeaverService(K6WeaverConfigProperties k6WeaverConfigProperties) {
        return new K6WeaverService(k6WeaverConfigProperties);
    }

    @Bean
    public K6ScriptController k6ScriptController(K6WeaverService k6WeaverService) {
        return new K6ScriptController(k6WeaverService);
    }
}
