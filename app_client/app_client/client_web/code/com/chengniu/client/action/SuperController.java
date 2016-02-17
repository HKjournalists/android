package com.chengniu.client.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.chengniu.client.common.CommonUtil;
import com.chengniu.client.common.Operator;
import com.chengniu.client.common.OperatorThreadLocal;
import com.chengniu.client.pojo.ResultInfo;
import com.chengniu.client.pojo.SearchVO;

public class SuperController {
	protected static final Logger log = LogManager
			.getLogger(SuperController.class);

	/**
	 * 获取当前登录操作者的信息
	 * 
	 * @return
	 */
	protected final Operator getCurrentOperator() {
		return OperatorThreadLocal.get();
	}

	public final void ajax(Object object) {
		this.ajax(object, "0", "");
	}

	public final void ajax(Object object, String code) {
		this.ajax(object, code, "");
	}

	public final void ajax(Object object, String code, String msg) {
		ResultInfo<Object> resultinfo = new ResultInfo<Object>();
		resultinfo.setCode(code);
		if (object instanceof String)
			try {
				resultinfo.setData(CommonUtil.gson().toJson(object.toString()));
			} catch (Exception e) {
				resultinfo.setData(object);
			}
		else
			resultinfo.setData(object);
		try {
			resultinfo.setMsg(msg);
		} catch (Exception e) {
			resultinfo.setMsg(msg);
		}
		try {
			HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes()).getResponse();
			response.setContentType("application/json;charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.write(CommonUtil.gson().toJson(resultinfo));
			writer.flush();
			writer.close();
		} catch (IOException e) {
		}
	}

	public final SearchVO getSearch() {
		SearchVO vo = this.getClientParam(SearchVO.class);
		if (vo == null)
			vo = new SearchVO();
		try {
			if (vo.getPageNum() != null)
				vo.setPageMaxResult(Integer
						.parseInt(((ServletRequestAttributes) RequestContextHolder
								.getRequestAttributes()).getRequest()
								.getParameter("pageSize")));
		} catch (Exception e) {
			vo.setPageMaxResult(20);
		}
		try {
			if (vo.getPageNum() == null)
				vo.setPageNum(((ServletRequestAttributes) RequestContextHolder
						.getRequestAttributes()).getRequest().getParameter(
						"pageNum"));
		} catch (Exception e) {
		}
		return vo;
	}

	public String getClientParam() {
		return ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest().getParameter("data");
	}

	public <T> T getClientParam(Class<T> t) {
		try {
			return CommonUtil
					.gson()
					.fromJson(
							((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
									.getRequest().getParameter("data"), t);
		} catch (Exception e) {
			log.info("处理失败", e);
		}
		return null;
	}
}