package com.k6.weaver.Controller;

import com.k6.weaver.Service.K6WeaverService;
import com.k6.weaver.config.annotation.K6Ignore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/k6")
public class K6ScriptController {
    private final K6WeaverService k6WeaverService;

    public K6ScriptController(K6WeaverService k6WeaverService) {
        this.k6WeaverService = k6WeaverService;
    }

    @GetMapping("/gen-script")
    @K6Ignore
    public ResponseEntity<String> generateK6Script() {
        String k6Script = k6WeaverService.createK6Script();
        return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=\"test.js\"").body(k6Script);
    }
}
