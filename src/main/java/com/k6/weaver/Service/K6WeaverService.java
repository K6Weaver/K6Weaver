package com.k6.weaver.Service;

import com.k6.weaver.generator.ControllerScanner;
import com.k6.weaver.generator.K6ScriptGenerator;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class K6WeaverService {
    // 이것을 사용자가 설정 할 수있게 K6WeaverConfig 만들어서 설정할 수 있게 해야함
    private static final String rootPath = "http://localhost:8080";

    public String createK6Script() {
        Set<String> endPointSet = ControllerScanner.fetchEndPoints();
        return K6ScriptGenerator.generateScript(endPointSet, rootPath);
    }
}
