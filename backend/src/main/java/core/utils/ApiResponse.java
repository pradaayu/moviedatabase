package core.utils;

import java.time.LocalDateTime;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String errorCode;
    private LocalDateTime timestamp;

    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(boolean success, String message, T data) {
        this();
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(boolean success, String message, T data, String errorCode) {
        this(success, message, data);
        this.errorCode = errorCode;
    }

    // Getters and setters
    public T getData() {
		return data;
	}
    public void setData(T data) {
    	this.data = data;
    }
    
    public boolean isSuccess() {
    	return this.success;
    }
    public void setSuccess(boolean success) {
    	this.success = success;
    }
    
    public String getMessage() {
    	return this.message;
    }
    public void setMessage(String message) {
    	this.message = message;
    }
    
    public String getErrorCode() {
    	return this.errorCode;
    }
    public void setErrorCode(String errCode) {
    	this.errorCode = errCode;
    }
    
    public LocalDateTime getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
    	this.timestamp = timestamp;
    }
}

