package com.kplus.car.container.module;

import com.kplus.car.container.command.DazeInvokedUrlCommand;

public class DazePhotoModule extends DazeModule implements DazeModuleDelegate{

	@Override
	public String methodsForJS() {
		return "\"photo\"";
	}
	
	public void photo(DazeInvokedUrlCommand command){
		viewController.photo(command);
	}

}
