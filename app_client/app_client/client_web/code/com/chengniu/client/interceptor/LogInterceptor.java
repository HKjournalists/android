package com.chengniu.client.interceptor;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Value;

import com.chengniu.client.common.CommonUtil;
import com.chengniu.client.common.Operator;
import com.chengniu.client.common.OperatorThreadLocal;
import com.chengniu.client.pojo.ResultInfo;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class LogInterceptor implements Interceptor {
	private static final long serialVersionUID = 8109886567899928146L;
	protected static final Logger log = LogManager
			.getLogger(LogInterceptor.class);

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		StringBuilder url = new StringBuilder();
		ServletActionContext.getResponse().setContentType(
				"application/json;charset=utf-8");
		ServletActionContext.getResponse().setHeader("Server", "Tengine/2.5.0");
		ServletActionContext.getResponse().setHeader(
				"Access-Control-Allow-Origin", "*");
		url.append(ServletActionContext.getRequest().getRequestURI());
		if (ServletActionContext.getRequest().getMethod()
				.equalsIgnoreCase("get")
				&& ServletActionContext.getRequest().getQueryString() != null) {
			url.append("?");
			url.append(ServletActionContext.getRequest().getQueryString());
		} else {
			Iterator<Entry<String, Object>> it = invocation
					.getInvocationContext().getParameters().entrySet()
					.iterator();
			url.append("?");
			while (it.hasNext()) {
				try {
					Entry<String, Object> entry = it.next();
					url.append("&").append(entry.getKey()).append("=");
					String[] value = (String[]) entry.getValue();
					url.append(value[0]);
				} catch (Exception e) {
				}
			}
		}
		// 获取签名方式的参数
		String data = null;
		Operator op = new Operator();
		op.setUserType(1);
		try {
			data = ((String[]) invocation.getInvocationContext()
					.getParameters().get("params"))[0].trim();
			op.setUserType(0);
		} catch (Exception e) {
		}
		try {
			data = ((String[]) invocation.getInvocationContext()
					.getParameters().get("data"))[0].trim();
		} catch (Exception e) {
		}
		try {
			Map<String, String> map = CommonUtil.gson().fromJson(
					data.toString(), new TypeToken<Map<String, String>>() {
					}.getType());
			Iterator<Entry<String, String>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> entry = it.next();
				invocation.getInvocationContext().getValueStack()
						.setValue(entry.getKey(), entry.getValue());
			}
		} catch (Exception e) {
		}
		log.info("{}请求地址{}", ServletActionContext.getRequest().getMethod(), url
				.toString().replace("?&", "?"));
		OperatorThreadLocal.set(op);
		try {
			return invocation.invoke();
		} catch (Exception e) {
			log.warn("app请求处理失败{}类方法{}执行参数{}", invocation.getProxy()
					.getAction(), invocation.getProxy().getMethod(), invocation
					.getInvocationContext().getParameters(), e);
		} finally {
			OperatorThreadLocal.clean();
		}
		ResultInfo<String> resultinfo = new ResultInfo<String>();
		resultinfo.setMsg("系统忙");
		invocation.getInvocationContext().put("message",
				CommonUtil.gson().toJson(resultinfo));
		return "ajax";
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init() {
	}

}