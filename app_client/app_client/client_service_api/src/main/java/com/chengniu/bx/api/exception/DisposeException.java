package com.chengniu.bx.api.exception;

public class DisposeException extends RuntimeException {
	private static final long serialVersionUID = 1995777499071564356L;

	private String message;
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public DisposeException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public DisposeException(String message, String code) {
		super();
		this.code = code;
		this.message = message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public DisposeException(String message, Integer code) {
		super();
		try {
			this.code = String.valueOf(code);
		} catch (Exception e) {
		}
		this.message = message;
	}

	public DisposeException(String message, String code, Throwable cause) {
		super(cause);
		this.code = code;
		this.message = message;
	}

	public DisposeException(String message, Throwable cause) {
		super(cause);
		this.message = message;
	}

	public DisposeException(String message, Integer code, Throwable cause) {
		super(cause);
		try {
			this.code = String.valueOf(code);
		} catch (Exception e) {
		}
		this.message = message;
	}
}
