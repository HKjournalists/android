package com.client.event;

import org.springframework.context.ApplicationEvent;

public class ContentEvent extends ApplicationEvent {

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ContentEvent(Object source) {
		super(source);
		// TODO Auto-generated constructor stub
	}

	public ContentEvent(Object source, String message) {
		super(source);
		this.message = message;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7613072558153409542L;

}
