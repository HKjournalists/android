package com.kplus.car.container.module;

import com.kplus.car.container.command.DazeInvokedUrlCommand;

public class DazePayModule extends DazeModule implements DazeModuleDelegate{

	@Override
	public String methodsForJS() {
		return "\"pay\"";
	}
	
	public void pay(DazeInvokedUrlCommand command){
		viewController.pay(command);
	}

}
