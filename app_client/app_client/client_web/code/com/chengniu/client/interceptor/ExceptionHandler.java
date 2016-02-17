package com.chengniu.client.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.chengniu.client.common.CommonUtil;
import com.chengniu.client.pojo.ResultInfo;

public class ExceptionHandler implements HandlerExceptionResolver {

	@Override
	@ResponseBody
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		try {
			response.setStatus(250);
			PrintWriter print = response.getWriter();
			ResultInfo<Object> resultinfo = new ResultInfo<Object>();
			resultinfo.setCode("250");
			resultinfo.setMsg("Tengine/2.5.0");
			print.write(CommonUtil.gson().toJson(resultinfo));
			print.flush();
			print.close();
		} catch (Exception e) {
		}
		return null;
	}

}