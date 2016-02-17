package com.kplus.car.container.command;

import com.kplus.car.container.module.DazeModule;

public interface WebViewOperateInterface {
	public void fetchQueue(String command);
	public DazeModule getCommandInstance(String moduleName);
}
