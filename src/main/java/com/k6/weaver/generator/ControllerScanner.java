package com.k6.weaver.generator;

import com.k6.weaver.config.annotation.K6Ignore;
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
    // private final String basePackage;

    public ControllerScanner(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void findEndPoints() {
        Map<String, Object> restController = applicationContext.getBeansWithAnnotation(RestController.class);

        for (Object controller : restController.values()) {
            Class<?> controllerClass = controller.getClass();
            String basePath = "";
            System.out.println(controller.getClass().getName());
            if (controllerClass.isAnnotationPresent(K6Ignore.class)) {
                continue;
            }

            if (controllerClass.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = controllerClass.getAnnotation(RequestMapping.class);
                basePath = requestMapping.value()[0];
            }

            Method[] methods = controllerClass.getDeclaredMethods();
            for (Method method : methods) {

                if (!method.isAnnotationPresent(K6Ignore.class) && method.isAnnotationPresent(GetMapping.class)) {
                    String fullPath = basePath + method.getAnnotation(GetMapping.class).value()[0];
                    endPoints.add(fullPath);
                }
            }
        }
    }

    public static Set<String> fetchEndPoints() {
        return endPoints;
    }
}
