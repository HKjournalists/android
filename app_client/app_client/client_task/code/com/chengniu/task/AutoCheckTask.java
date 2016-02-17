package com.chengniu.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service("checkStatusTask")
public class AutoCheckTask {
	protected static final Logger log = LogManager
			.getLogger(AutoCheckTask.class);
	public void execute() throws Exception {
		log.info("开始");
	}
}