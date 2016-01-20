package com.client.event;

import org.springframework.context.ApplicationListener;

public class ContentListener implements ApplicationListener<ContentEvent> {

	@Override
	public void onApplicationEvent(ContentEvent event) {
		System.err.println("ContentListener收到一个牛逼的事件" + event.getSource()
				+ event.getMessage());
	}
}