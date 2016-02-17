package com.chengniu.client.interceptor;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.chengniu.client.common.CommonUtil;
import com.chengniu.client.common.Operator;
import com.chengniu.client.common.OperatorThreadLocal;
import com.chengniu.client.pojo.ProviderApp;
import com.chengniu.client.pojo.ResultInfo;
import com.chengniu.client.service.AppKeyService;
import com.google.common.reflect.TypeToken;
import com.kplus.orders.execption.DisposeException;
import com.kplus.orders.rpc.common.MD5Util;

public class SpringProviderInterceptor implements HandlerInterceptor {
	protected static final Logger log = LogManager
			.getLogger(SpringProviderInterceptor.class);
	@Resource(name = "appKeyService")
	private AppKeyService appKeyService;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		response.setContentType("application/json;charset=utf-8");
		String result = null;
		try {
			Map<String, String[]> vs = request.getParameterMap();
			String timestamp;
			String sign;
			Operator op = null;
			String data = "";
			String appId;
			ProviderApp app;
			try {
				appId = vs.get("appId")[0].trim();
				timestamp = vs.get("timestamp")[0].trim();
				sign = vs.get("sign")[0].trim();
				try {
					data = vs.get("data")[0].trim();
				} catch (Exception e) {
				}
				if (appId.length() <= 0 || sign.length() <= 0)
					throw new DisposeException("提交的参数不正确");
				app = this.appKeyService.queryProviderAppById(appId);
				if (app == null)
					throw new DisposeException("提交的参数不正确");
				long time = (new Date().getTime() - Long.parseLong(timestamp)) / 1000;
				if (time > 1720 || time < -1720)
					throw new DisposeException(
							"签名验证失败,签名过期,请调整时间为"
									+ new SimpleDateFormat("yyyy-MM-dd HH:mm")
											.format(new Date()));
			} catch (DisposeException e) {
				throw e;
			} catch (Exception e) {
				throw new DisposeException("提交的参数不正确");
			}
			if (app.getType() == null) {
				app.setType(0);
			}
			String encrypt = MD5Util.encrypt(app.getId() + app.getKey()
					+ timestamp + data);
			// 验证签名是否正确
			if (sign.equalsIgnoreCase(encrypt)) {
				op = new Operator();
				Map<String, String> map = CommonUtil.gson().fromJson(
						data.toString(), new TypeToken<Map<String, String>>() {
							private static final long serialVersionUID = -4588458635046506303L;
						}.getType());
				Iterator<Entry<String, String>> it = map.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, String> entry = it.next();
					if ("userId".equals(entry.getKey()))
						op.setUserId(entry.getValue());
					// vs.put(entry.getKey(), new String[] { entry.getValue()
					// });
				}
				op.setUserType(1);
				OperatorThreadLocal.set(op);
				return true;
			} else
				throw new DisposeException("系统忙");
		} catch (DisposeException e) {
			log.warn("app请求处理失败{}", request.getRequestURI());
			result = e.getMessage();
		} catch (Exception e) {
			log.warn("app请求处理失败{}", request.getRequestURI());
		}
		ResultInfo<String> resultinfo = new ResultInfo<String>();
		resultinfo.setCode("999");
		if (result == null)
			result = "系统忙";
		resultinfo.setMsg(result);
		try {
			PrintWriter writer = response.getWriter();
			writer.write(CommonUtil.gson().toJson(resultinfo));
			writer.flush();
			writer.close();
		} catch (Exception e) {
		}
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		System.err.println("请求");
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		System.err.println("返回");
	}
}