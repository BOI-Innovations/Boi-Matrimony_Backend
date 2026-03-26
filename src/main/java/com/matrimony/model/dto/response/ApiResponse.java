package com.matrimony.model.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
	private Boolean success;
	private String message;
	private Object data;
	private LocalDateTime timestamp;
	private Integer status;

	public ApiResponse() {
		this.timestamp = LocalDateTime.now();
	}

	public ApiResponse(Boolean success, String message) {
		this();
		this.success = success;
		this.message = message;
	}

	public ApiResponse(Boolean success, String message, Object data) {
		this();
		this.success = success;
		this.message = message;
		this.data = data;
	}

	public ApiResponse(Boolean success, String message, Object data, Integer status) {
		this();
		this.success = success;
		this.message = message;
		this.data = data;
		this.status = status;
	}

	// Static factory methods
	public static ApiResponse success(String message) {
		return new ApiResponse(true, message);
	}

	public static ApiResponse success(String message, Object data) {
		return new ApiResponse(true, message, data);
	}

	public static ApiResponse error(String message) {
		return new ApiResponse(false, message);
	}

	public static ApiResponse error(String message, Integer status) {
		return new ApiResponse(false, message, null, status);
	}

	// Getters and Setters
	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
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

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}