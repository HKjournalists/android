package com.client.event;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppEventMulticaster extends
		org.springframework.context.event.SimpleApplicationEventMulticaster {
	private Executor taskExecutor;

	@Override
	protected Executor getTaskExecutor() {
		if (this.taskExecutor == null)
			this.taskExecutor = Executors.newFixedThreadPool(80);
		return taskExecutor;
	}
}