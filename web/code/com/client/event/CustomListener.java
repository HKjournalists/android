package com.client.event;

import org.springframework.context.ApplicationListener;

public class CustomListener implements ApplicationListener<CustomEvent> {

	@Override
	public void onApplicationEvent(CustomEvent event) {
		System.err.println("CustomListener收到一个牛逼的事件" + event.getSource());
	}
}