package com.accolite.pru.health.AuthApp.model.payload;

public class ApiResponse {

    private String data;
    private Boolean success;

    public ApiResponse() {
    }

    public ApiResponse(String data, Boolean success) {
        this.data = data;
        this.success = success;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
