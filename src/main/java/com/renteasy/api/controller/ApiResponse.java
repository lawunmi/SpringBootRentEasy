package com.renteasy.api.controller;

public class ApiResponse {
    private String status;
    private String message;
    private Object data;
    
    
    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
    public static ApiResponse success(String message, Object data) {
        ApiResponse response = new ApiResponse();
        response.setStatus("successful");
        response.setMessage(message);
        response.setData(data);
        return response;
    }
    
    public static ApiResponse error(String message) {
        ApiResponse response = new ApiResponse();
        response.setStatus("failed");
        response.setMessage(message);
        return response;
    }

	
}