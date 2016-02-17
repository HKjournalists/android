package com.chengniu.client.event.dispatcher;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import org.springframework.stereotype.Component;

@Component("eventDispatcher")
public class EventDispatcherImpl implements EventDispatcher {

	private static EventBus eventBus;
	
	{
//		int coreNum = Runtime.getRuntime().availableProcessors();
//		eventBus = new AsyncEventBus(new ThreadPoolExecutor(
//				coreNum * 2,
//				coreNum * 2,
//				600,
//				TimeUnit.MINUTES,
//				new LinkedBlockingQueue<Runnable>(10000),
//				new ThreadFactory() {
//
//					@Override
//					public Thread newThread(Runnable r) {
//						Thread thread = new Thread(r);
//						thread.setName("EventButDriver");
//						thread.setDaemon(true);
//						thread.setPriority(Thread.NORM_PRIORITY);
//
//						return thread;
//					}
//
//		}, new CallerRunsPolicy()));

        eventBus = new EventBus();
	}

	@Override
	public void register(Object ovserver) {
		eventBus.register(ovserver);
	}
	
	@Override
	public void publish(Object event) {
		eventBus.post(event);
	}
	
}
