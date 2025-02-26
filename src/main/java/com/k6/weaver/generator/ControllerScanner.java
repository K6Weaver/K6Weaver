package com.k6.weaver.generator;

import com.k6.weaver.config.annotation.K6Ignore;
import com.k6.weaver.util.K6WeaverConfigProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class ControllerScanner {
    private static final Set<String> endPoints = new HashSet<>();
    private final ApplicationContext applicationContext;
    private final K6WeaverConfigProperties k6WeaverConfigProperties;

    public ControllerScanner(ApplicationContext applicationContext, K6WeaverConfigProperties k6WeaverConfigProperties) {
        this.applicationContext = applicationContext;
        this.k6WeaverConfigProperties = k6WeaverConfigProperties;
    }

    @PostConstruct
    public void findEndPoints() {
        Map<String, Object> restController = applicationContext.getBeansWithAnnotation(RestController.class);
        String basePackage = k6WeaverConfigProperties.getBasePackage();

        System.out.println("Base Package: " + basePackage);

        for (Object controller : restController.values()) {
            Class<?> controllerClass = controller.getClass();
            String controllerPackage = controllerClass.getPackage().getName();
            String baseEndPoint = "";

            if (!controllerPackage.startsWith(basePackage) || controllerClass.isAnnotationPresent(K6Ignore.class)) {
                continue;
            }
            System.out.println(controller.getClass().getName());
            if (controllerClass.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = controllerClass.getAnnotation(RequestMapping.class);
                baseEndPoint = requestMapping.value()[0];
            }
            Method[] methods = controllerClass.getDeclaredMethods();
            for (Method method : methods) {
                if (!method.isAnnotationPresent(K6Ignore.class) && method.isAnnotationPresent(GetMapping.class)) {
                    String fullEndPoint = baseEndPoint + method.getAnnotation(GetMapping.class).value()[0];
                    endPoints.add(fullEndPoint);
                }
            }
        }
    }

    public static Set<String> fetchEndPoints() {
        return endPoints;
    }
}
