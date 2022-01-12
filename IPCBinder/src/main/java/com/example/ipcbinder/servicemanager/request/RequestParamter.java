package com.example.ipcbinder.servicemanager.request;

public class RequestParamter {

    private String parameterClassName;
    private String parameterValue;


    public RequestParamter() {
    }

    public RequestParamter(String parameterClassName, String parameterValue) {

        this.parameterClassName = parameterClassName;
        this.parameterValue = parameterValue;
    }

    public String getParameterClassName() {
        return parameterClassName;
    }

    public void setParameterClassName(String parameterClassName) {
        this.parameterClassName = parameterClassName;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }
}
