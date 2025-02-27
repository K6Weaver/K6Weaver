package com.k6.weaver.generator;

import com.k6.weaver.config.annotation.K6Ignore;
import com.k6.weaver.util.Endpoint;
import com.k6.weaver.util.K6WeaverConfigProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.*;

@Component
public class ControllerScanner {
    private static final List<Endpoint> endPoints = new ArrayList<>();
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

        for (Object controller : restController.values()) {
            Class<?> controllerClass = controller.getClass();
            String controllerPackage = controllerClass.getPackage().getName();
            String baseEndPoint = "";

            if (!controllerPackage.startsWith(basePackage) || controllerClass.isAnnotationPresent(K6Ignore.class)) {
                continue;
            }

            if (controllerClass.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = controllerClass.getAnnotation(RequestMapping.class);
                baseEndPoint = endPointFormatter(requestMapping.value()[0]);
            }
            Method[] methods = controllerClass.getDeclaredMethods();
            for (Method method : methods) {

                if (!method.isAnnotationPresent(K6Ignore.class)) {
                    Endpoint targetEndPoint = null;
                    if (method.isAnnotationPresent(GetMapping.class)) {

                        String fullEndPoint = baseEndPoint;
                        String[] urlValues = method.getAnnotation(GetMapping.class).value();
                        if (urlValues.length > 0) {
                            fullEndPoint += endPointFormatter(urlValues[0]);
                        }

                        targetEndPoint = new Endpoint(fullEndPoint, "GET", controllerPackage);
                    } else if (method.isAnnotationPresent(PostMapping.class)) {
                        String fullEndPoint = baseEndPoint;
                        String[] urlValues = method.getAnnotation(PostMapping.class).value();
                        if (urlValues.length > 0) {
                            fullEndPoint += endPointFormatter(urlValues[0]);
                        }
                        targetEndPoint = new Endpoint(fullEndPoint, "POST", controllerPackage);
                    }
                    if (targetEndPoint == null) {
                        continue;
                    }
                    endPoints.add(targetEndPoint);
                }
            }
        }
    }

    public static List<Endpoint> fetchEndPoints() {
        Collections.sort(endPoints);
        return endPoints;
    }

    private String endPointFormatter(String endPoint) {
        if (!endPoint.startsWith("/")) {
            endPoint = "/" + endPoint;
        }
        if (endPoint.endsWith("/")) {
            endPoint = endPoint.substring(0, endPoint.length() - 1);
        }
        return endPoint;
    }
}
