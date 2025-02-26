package com.k6.weaver.generator;

import java.util.Set;

public class K6ScriptGenerator {
    public static String generateScript(Set<String> endPointSet, String baseUrl) {
        StringBuilder k6Script = new StringBuilder();
        k6Script.append("import http from 'k6/http';\n");
        k6Script.append("import { check, sleep } from 'k6';\n\n");
        k6Script.append("// Here write your base URL\n");
        k6Script.append("const baseUrl = '").append(baseUrl).append("';").append("\n");

        k6Script.append("export let options = {\n" +
                "  stages: [\n" +
                "    { duration: \"1m\", target: 50 },\n" +
                "    { duration: \"2m\", target: 100 },\n" +
                "    { duration: \"1m\", target: 0 },\n" +
                "  ],\n"+
                "};").append("\n");

        k6Script.append("export default function () {\n");

        for (String endpoint : endPointSet) {
            k6Script.append("    let res = http.get(`${baseUrl}").append(endpoint).append("`);\n");
            k6Script.append("    check(res, { 'status was 200': (r) => r.status == 200 });\n");
        }

        k6Script.append("    sleep(1);").append("\n");
        k6Script.append("}\n");
        return k6Script.toString();
    }
}
