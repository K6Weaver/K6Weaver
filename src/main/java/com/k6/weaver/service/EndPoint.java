package com.k6.weaver.service;

public class EndPoint implements Comparable<EndPoint> {
    private String url;
    private String reqMethod;
    private String packagePath;
    private String payloadName;

    public EndPoint(String url, String reqMethod, String packagePath) {
        this.url = url;
        this.reqMethod = reqMethod;
        this.packagePath = packagePath;
    }

    @Override
    public int compareTo(EndPoint o) {
        if (packagePath.equals(o.getPackagePath())) {
            if (reqMethod.equals(o.getReqMethod())) {
                return url.compareTo(o.url);
            }
            return o.reqMethod.compareTo(reqMethod);
        }
        return packagePath.compareTo(o.packagePath);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReqMethod() {
        return reqMethod;
    }

    public void setReqMethod(String reqMethod) {
        this.reqMethod = reqMethod;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public String getPayloadName() {
        return payloadName;
    }

    public void setPayloadName(String payloadName) {
        this.payloadName = payloadName;
    }
}
