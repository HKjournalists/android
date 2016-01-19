package com.client.common;

import java.util.Iterator;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class SpringLogInterceptor implements HandlerInterceptor {
	protected static final Logger log = LogManager
			.getLogger(SpringLogInterceptor.class);
	@Value("${url.http:}")
	private String http;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		StringBuilder url = new StringBuilder();
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Server", "Tengine/2.5.0");
		// 跨域
		response.setHeader("Access-Control-Allow-Origin", "*");
		url.append(request.getRequestURI());
		if (request.getMethod().equalsIgnoreCase("get")
				&& request.getQueryString() != null) {
			url.append("?");
			url.append(request.getQueryString());
		} else {
			Iterator<Entry<String, String[]>> it = request.getParameterMap()
					.entrySet().iterator();
			url.append("?");
			while (it.hasNext()) {
				try {
					Entry<String, String[]> entry = it.next();
					url.append("&").append(entry.getKey()).append("=");
					String[] value = entry.getValue();
					url.append(value[0]);
				} catch (Exception e) {
				}
			}
		}
		String requestUrl = url.toString().replace("?&", "?");
		if (requestUrl.endsWith("?"))
			requestUrl = requestUrl.replace("?", "");
		log.info("请求地址{}", requestUrl);
//		if (!url.toString().startsWith("/app")) {
//			response.sendRedirect(http + url.toString());
//			return false;
//		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}