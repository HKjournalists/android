package com.chengniu.client.action;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.chengniu.client.common.CommonUtil;
import com.chengniu.client.common.DateUtils;
import com.chengniu.client.common.Operator;
import com.chengniu.client.common.OperatorThreadLocal;
import com.chengniu.client.pojo.ResultInfo;
import com.chengniu.client.pojo.SearchVO;
import com.kplus.orders.rpc.dto.PageVO;
import com.opensymphony.xwork2.ActionSupport;

public class SuperAction extends ActionSupport {
	private static final long serialVersionUID = -3356343070710793029L;
	protected static final Logger log = LogManager.getLogger(SuperAction.class);
	public static final String REDIRECT = "redirect";
	public static final String BOOLEAN_RESULT = "result";
	public static final String AJAX = "ajax";
	protected String returnURL;
	private String pageSize;
	protected PageVO<?> page;
	protected SearchVO search;
	protected String clientParam;
	protected Boolean result;
	protected String pageNum;

	public final String getReturnURL() {
		return returnURL;
	}

	public final void setReturnURL(String returnURL) {
		this.returnURL = returnURL;
	}

	// ajax返回信息
	protected String message;

	public final String getMessage() {
		return message;
	}

	/**
	 * 获取当前登录操作者的信息
	 * 
	 * @return
	 */
	protected final Operator getCurrentOperator() {
		return OperatorThreadLocal.get();
	}

	public final Operator getOperator() {
		return this.getCurrentOperator();
	}

	public final String ajax(Object object) {
		return this.ajax(object, "0", "");
	}

	public final String ajax(Object object, String code) {
		return this.ajax(object, code, "");
	}

	public final String ajax(Object object, String code, String msg) {
		ResultInfo<Object> resultinfo = new ResultInfo<Object>();
		resultinfo.setCode(code);
		try {
			if (object instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>) object;
				// 返回当前时间
				map.put("time",
						DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			}
		} catch (Exception e1) {
		}
		if (object instanceof String)
			try {
				resultinfo.setData(this.getText(object.toString()));
			} catch (Exception e) {
				resultinfo.setData(object);
			}
		else
			resultinfo.setData(object);
		try {
			resultinfo.setMsg(this.getText(msg));
		} catch (Exception e) {
			resultinfo.setMsg(msg);
		}
		this.message = CommonUtil.gson().toJson(resultinfo);
		try {
			PrintWriter w = ServletActionContext.getResponse().getWriter();
			w.write(this.getMessage());
			w.flush();
			w.close();
			return NONE;
		} catch (Exception e) {
			return "ajax";
		}
	}

	public final PageVO<?> getPage() {
		return page;
	}

	protected final void setPage(PageVO<?> page) {
		this.page = page;
	}

	public final SearchVO getSearch() {
		if (search == null)
			search = new SearchVO();
		try {
			search.setPageMaxResult(Integer.parseInt(this.getPageSize()));
		} catch (Exception e) {
			search.setPageMaxResult(20);
		}
		search.setPageNum(this.getPageNum());
		return search;
	}

	public final void setSearch(SearchVO search) {
		this.search = search;
	}

	public final String getPageNum() {
		return pageNum;
	}

	public final void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public final void setMessage(String message) {
		this.message = message;
	}

	public final Boolean getResult() {
		return result;
	}

	public final void setResult(Boolean result) {
		this.result = result;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getClientParam() {
		return clientParam;
	}

	public void setClientParam(String clientParam) {
		this.clientParam = clientParam;
	}
}