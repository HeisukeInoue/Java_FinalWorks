package com.example.dockerapi.dto;

public class apiResponse<T> {
    private T data;
    private String error;
    
    private apiResponse(T data, String error) {
        this.data = data;
        this.error = error;
    }
    
    public static <T> apiResponse<T> success(T data) {
        return new apiResponse<>(data, null);
    }
    
    public static <T> apiResponse<T> error(String error) {
        return new apiResponse<>(null, error);
    }
    
    public boolean isSuccess() {
        return data != null;
    }
    
    public T getData() {
        return data;
    }
    
    public String getError() {
        return error;
    }
}