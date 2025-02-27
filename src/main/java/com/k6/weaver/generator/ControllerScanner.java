package com.k6.weaver.generator;

import com.k6.weaver.config.annotation.K6Ignore;
import com.k6.weaver.service.EndPoint;
import com.k6.weaver.util.K6WeaverConfigProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.*;

@Component
public class ControllerScanner {
    private static final List<EndPoint> END_POINTS = new ArrayList<>();
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
                if (method.isAnnotationPresent(K6Ignore.class)) {
                    continue;
                }

                EndPoint targetEndPoint = getTargetEndPoint(method, baseEndPoint, controllerPackage);
                if (targetEndPoint == null) {
                    continue;
                }
                END_POINTS.add(targetEndPoint);
            }
        }
    }

    public static List<EndPoint> fetchEndPoints() {
        Collections.sort(END_POINTS);
        return END_POINTS;
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

    private EndPoint getTargetEndPoint(Method method, String baseEndPoint, String controllerPackage) {

        EndPoint targetEndPoint = null;
        if (method.isAnnotationPresent(GetMapping.class)) {
            targetEndPoint = generateTargetEndPoint(baseEndPoint,
                    method.getAnnotation(GetMapping.class).value(), "GET", controllerPackage);
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            targetEndPoint = generateTargetEndPoint(baseEndPoint,
                    method.getAnnotation(PostMapping.class).value(), "POST", controllerPackage);
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            targetEndPoint = generateTargetEndPoint(baseEndPoint,
                    method.getAnnotation(PutMapping.class).value(), "PUT", controllerPackage);
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            targetEndPoint = generateTargetEndPoint(baseEndPoint,
                    method.getAnnotation(DeleteMapping.class).value(), "DELETE", controllerPackage);
        }
        return targetEndPoint;
    }

    private EndPoint generateTargetEndPoint(String baseEndPoint, String[] urlValues,
                                            String requestMethod, String controllerPackage) {
        String fullEndPoint = baseEndPoint;
        if (urlValues.length > 0) {
            fullEndPoint += endPointFormatter(urlValues[0]);
        }
        return new EndPoint(fullEndPoint, requestMethod, controllerPackage);
    }
}
