package com.chengniu.client.event.dispatcher;


public interface EventDispatcher {

	void register(Object obvserver);
	
	void publish(Object event);

}
