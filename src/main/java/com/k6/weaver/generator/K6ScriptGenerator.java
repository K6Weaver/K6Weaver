package com.k6.weaver.generator;

import com.k6.weaver.service.EndPoint;

import java.util.List;

public class K6ScriptGenerator {
    public static String generateScript(List<EndPoint> endPointSet, String baseUrl) {
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

        for (EndPoint endPoint : endPointSet) {
            if (endPoint.getReqMethod().equals("GET") || endPoint.getReqMethod().equals("DELETE")) {
                continue;
            }
            String[] splitUrl = endPoint.getUrl().split("/");
            String payloadName = "";
            boolean isFirstWord = true;

            for (String urlPattern : splitUrl) {
                if (urlPattern.isBlank() || urlPattern.toLowerCase().startsWith("api") || isVersion(urlPattern)) {
                    continue;
                }

                urlPattern = urlPattern.replace("{", "");
                urlPattern = urlPattern.replace("}", "");
                urlPattern = urlPattern.replace("-", "");
                urlPattern = urlPattern.replace(" ", "");
                if (isFirstWord) {
                    payloadName += urlPattern;
                    isFirstWord = false;
                } else {
                    payloadName += urlPattern.substring(0, 1).toUpperCase() + urlPattern.substring(1);
                }
            }

            if (payloadName.isBlank()) {
                payloadName = "api";
            }
            payloadName += "Payload";
            endPoint.setPayloadName(payloadName);
            k6Script.append("   let " + payloadName + " = /* write body here! */ null;\n");
        }

        k6Script.append("const token = 'your_token_here';\n");
        k6Script.append("   let params = {\n" +
                "           headers: {\n" +
                "               'Content-Type': 'application/json',\n" +
                "               // 'Authorization': `Bearer ${token}`,\n" +
                "           },\n" +
                "       };\n");
        k6Script.append("let res;\n");

        for (EndPoint endpoint : endPointSet) {
            if (!beforePackage.equals(endpoint.getPackagePath())) {
                beforePackage = endpoint.getPackagePath();
                k6Script.append("\n/* ========== ").append(endpoint.getPackagePath()).append(" ========== */\n");
            }

            if (endpoint.getReqMethod().equals("GET")) {
                k6Script.append("    res = http.get(`${baseUrl}").append(endpoint.getUrl()).append("`);\n");
            } else if (endpoint.getReqMethod().equals("DELETE")) {
                k6Script.append("    res = http.del(`${baseUrl}").append(endpoint.getUrl()).append("`);\n");
            } else if (endpoint.getReqMethod().equals("POST")) {
                k6Script.append("    res = http.post(`${baseUrl}").append(endpoint.getUrl())
                        .append("`,").append(endpoint.getPayloadName()).append(", params);\n");
            } else if (endpoint.getReqMethod().equals("PUT")) {
                k6Script.append("    res = http.put(`${baseUrl}").append(endpoint.getUrl())
                        .append("`,").append(endpoint.getPayloadName()).append(", params);\n");
            } else {
                continue;
            }
            k6Script.append("    check(res, { 'status was 2xx': (r) => r.status >= 200 && r.status < 300 });\n");
        }

        k6Script.append("    sleep(1);").append("\n");
        k6Script.append("}\n");
        return k6Script.toString();
    }

    private static boolean isVersion(String urlPattern) {
        if (urlPattern.length() >= 2 && (urlPattern.startsWith("v") || urlPattern.startsWith("V")) &&
                Character.isDigit(urlPattern.charAt(1))) {
            return true;
        }
        return false;
    }
}
