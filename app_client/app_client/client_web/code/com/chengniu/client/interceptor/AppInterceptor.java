package com.chengniu.client.interceptor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.chengniu.client.common.CommonUtil;
import com.chengniu.client.common.Operator;
import com.chengniu.client.common.OperatorThreadLocal;
import com.chengniu.client.pojo.PlatUser;
import com.chengniu.client.pojo.ProviderApp;
import com.chengniu.client.pojo.ResultInfo;
import com.chengniu.client.service.AppKeyService;
import com.google.common.reflect.TypeToken;
import com.kplus.orders.execption.DisposeException;
import com.kplus.orders.rpc.common.MD5Util;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.util.ValueStack;

public class AppInterceptor implements Interceptor {
	private static final long serialVersionUID = 8109886567899928146L;
	protected static final Logger log = LogManager
			.getLogger(AppInterceptor.class);
	@Resource(name = "appKeyService")
	private AppKeyService appKeyService;

	@Override
	public void destroy() {
	}

	@Override
	public void init() {
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		String result = null;
		try {
			Map<String, Object> pam = invocation.getInvocationContext()
					.getParameters();
			Operator op = new Operator();
			Object appId;
			String data = "";
			boolean sign = false;
			appId = pam.get("appkey");
			if (appId == null) {
				appId = pam.get("appId");
				op.setUserType(1);
				sign = this.provider(invocation);
				try {
					data = ((String[]) pam.get("data"))[0].trim();
				} catch (Exception e) {
				}
			} else {
				op.setUserType(0);
				sign = this.user(invocation);
				try {
					data = ((String[]) pam.get("params"))[0].trim();
				} catch (Exception e) {
				}
			}
			if (appId == null)
				throw new DisposeException("提交的参数不正确");
			if (sign) {
				Map<String, String> map = CommonUtil.gson().fromJson(
						data.toString(), new TypeToken<Map<String, String>>() {
							private static final long serialVersionUID = -4588458635046506303L;
						}.getType());
				Iterator<Entry<String, String>> it = map.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, String> entry = it.next();
					if ("userId".equals(entry.getKey()))
						op.setUserId(entry.getValue());
					invocation.getInvocationContext().getValueStack()
							.setValue(entry.getKey(), entry.getValue());
				}
				OperatorThreadLocal.set(op);
				return invocation.invoke();
			} else
				throw new DisposeException("系统忙");
		} catch (DisposeException e) {
			log.info("app提交的参数不正确{}类方法{}执行参数{}", invocation.getProxy()
					.getAction(), invocation.getProxy().getMethod(), invocation
					.getInvocationContext().getParameters());
			result = e.getMessage();
		} catch (Exception e) {
			log.warn("app请求处理失败{}类方法{}执行参数{}", invocation.getProxy()
					.getAction(), invocation.getProxy().getMethod(), invocation
					.getInvocationContext().getParameters(), e);
		}
		ResultInfo<String> resultinfo = new ResultInfo<String>();
		resultinfo.setCode("999");
		if (result == null) {
			result = "系统忙";
		}
		resultinfo.setMsg(result);
		invocation.getInvocationContext().put("message",
				CommonUtil.gson().toJson(resultinfo));
		return "ajax";
	}

	private boolean user(ActionInvocation invocation) {
		try {
			Map<String, Object> pam = invocation.getInvocationContext()
					.getParameters();
			String sign;
			String timestamp;
			String data = "";
			String appId;
			PlatUser app;
			try {
				data = ((String[]) pam.get("params"))[0].trim();
			} catch (Exception e) {
			}
			appId = ((String[]) pam.get("appkey"))[0].trim();
			timestamp = ((String[]) pam.get("time"))[0].trim();
			sign = ((String[]) pam.get("sign"))[0].trim();
			if (appId.length() <= 0 || sign.length() <= 0)
				throw new DisposeException("提交的参数不正确");
			ValueStack vs = invocation.getInvocationContext().getValueStack();
			vs.setValue("sign", null);
			vs.setValue("appkey", null);
			vs.setValue("params", null);
			vs.setValue("clientParam", data);
			vs.setValue("time", null);
			app = this.appKeyService.queryByAppKey(appId);
			if (app == null)
				throw new DisposeException("提交的参数不正确");
			long time = (new Date().getTime() - Long.parseLong(timestamp)) / 1000;
			if ((time > 1720 || time < -1720))
				throw new DisposeException(
						"时间不一致,请调整时间为"
								+ new SimpleDateFormat("yyyy-MM-dd HH:mm")
										.format(new Date()));
			String encrypt = MD5Util.encrypt(app.getAppKey()
					+ app.getAppSecret() + timestamp + data);
			return sign.equalsIgnoreCase(encrypt);
		} catch (DisposeException e) {
			throw e;
		} catch (Exception e) {
			throw new DisposeException("提交的参数不正确");
		}
	}

	private boolean provider(ActionInvocation invocation) {
		try {
			Map<String, Object> pam = invocation.getInvocationContext()
					.getParameters();
			String timestamp;
			String data = "";
			String sign;
			String appId;
			ProviderApp app;
			appId = ((String[]) pam.get("appId"))[0].trim();
			timestamp = ((String[]) pam.get("timestamp"))[0].trim();
			sign = ((String[]) pam.get("sign"))[0].trim();
			try {
				data = ((String[]) pam.get("data"))[0].trim();
			} catch (Exception e) {
			}
			if (appId.length() <= 0 || sign.length() <= 0)
				throw new DisposeException("提交的参数不正确");
			ValueStack vs = invocation.getInvocationContext().getValueStack();
			vs.setValue("sign", null);
			vs.setValue("appId", null);
			vs.setValue("data", null);
			vs.setValue("clientParam", data);
			vs.setValue("timestamp", null);
			app = this.appKeyService.queryProviderAppById(appId);
			if (app == null)
				throw new DisposeException("提交的参数不正确");
			long time = (new Date().getTime() - Long.parseLong(timestamp)) / 1000;
			if (time > 1720 || time < -1720)
				throw new DisposeException(
						"签名验证失败,签名过期,请调整时间为"
								+ new SimpleDateFormat("yyyy-MM-dd HH:mm")
										.format(new Date()));
			if (app.getType() == null) {
				app.setType(0);
			}
			String encrypt = MD5Util.encrypt(app.getId() + app.getKey()
					+ timestamp + data);
			return sign.equalsIgnoreCase(encrypt);
		} catch (DisposeException e) {
			throw e;
		} catch (Exception e) {
			throw new DisposeException("提交的参数不正确");
		}
	}
}