package com.k6.weaver.generator;

import com.k6.weaver.Service.Endpoint;

import java.util.List;

public class K6ScriptGenerator {
    public static String generateScript(List<Endpoint> endPointSet, String baseUrl) {
        StringBuilder k6Script = new StringBuilder();
        String beforePackage = "";

        k6Script.append("import http from 'k6/http';\n");
        k6Script.append("import { check, sleep } from 'k6';\n\n");
        k6Script.append("// Here write your base URL\n");
        k6Script.append("const baseUrl = '").append(baseUrl).append("';").append("\n");

        k6Script.append("export let options = {\n" +
                "  stages: [\n" +
                "    { duration: \"1m\", target: 50 },\n" +
                "    { duration: \"2m\", target: 100 },\n" +
                "    { duration: \"1m\", target: 0 },\n" +
                "  ],\n" +
                "};").append("\n");

        k6Script.append("export default function () {\n");
        k6Script.append("   let params = {\n" +
                "           headers: {\n" +
                "               'Content-Type': 'application/json',\n" +
                "           },\n" +
                "       };\n");

        k6Script.append("let res;\n");

        for (Endpoint endpoint : endPointSet) {

            if (!beforePackage.equals(endpoint.getPackagePath())) {
                beforePackage = endpoint.getPackagePath();
                k6Script.append("\n/* ========== ").append(endpoint.getPackagePath()).append(" ========== */\n");
            }

            if (endpoint.getReqMethod().equals("GET")) {
                k6Script.append("    res = http.get(`${baseUrl}").append(endpoint.getUrl()).append("`);\n");
                k6Script.append("    check(res, { 'status was 200': (r) => r.status == 200 });\n");
            } else {
                k6Script.append("    res = http.post(`${baseUrl}").append(endpoint.getUrl()).append("`, , params);\n");
                k6Script.append("    check(res, { 'status was 201': (r) => r.status == 201 });\n");
            }
        }

        k6Script.append("    sleep(1);").append("\n");
        k6Script.append("}\n");
        return k6Script.toString();
    }
}
