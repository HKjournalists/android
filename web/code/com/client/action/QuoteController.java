package com.client.action;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.client.event.ContentEvent;

@Controller
@RequestMapping("/quote")
public class QuoteController {
	protected static final Logger log = LogManager
			.getLogger(QuoteController.class);

	@ResponseBody
	@RequestMapping("/request/{id}")
	public String request(@PathVariable String id) throws Exception {
		log.info("request{}", id);
		return id;
	}

	@ResponseBody
	@RequestMapping(value = "/notify/{quoteId}", method = RequestMethod.POST)
	public String notify(@RequestBody String body, @PathVariable String quoteId)
			throws Exception {
		try {
			log.info("返回{}数据{}", quoteId, body);
			ContextLoaderListener.getCurrentWebApplicationContext()
					.publishEvent(new ContentEvent(this, "kafka牛逼啊"));
		} catch (Exception e) {
			((ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes()).getResponse().setStatus(250);
			return "fail";
		}
		return "success";
	}

	@ResponseBody
	@RequestMapping(value = "/payback/{id}")
	public String payback(@RequestBody String body, @PathVariable String id)
			throws Exception {
		try {
			log.info("支付结果返回{}数据{}", id, body);
		} catch (Exception e) {
			((ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes()).getResponse().setStatus(250);
			return "fail";
		}
		return "success";
	}

}