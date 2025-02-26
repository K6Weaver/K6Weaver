package com.k6.weaver.Service;

import com.k6.weaver.generator.ControllerScanner;
import com.k6.weaver.generator.K6ScriptGenerator;
import com.k6.weaver.util.K6WeaverConfigProperties;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class K6WeaverService {
    private final K6WeaverConfigProperties k6WeaverConfigProperties;

    public K6WeaverService(K6WeaverConfigProperties k6WeaverConfigProperties) {
        this.k6WeaverConfigProperties = k6WeaverConfigProperties;
    }

    public String createK6Script() {
        Set<String> endPointSet = ControllerScanner.fetchEndPoints();
        String baseUrl = k6WeaverConfigProperties.getBaseUrl();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        return K6ScriptGenerator.generateScript(endPointSet, baseUrl);
    }
}
