package com.kplus.car;

import java.util.Map;


public interface Request<T extends Response> {
	/***
	 * 获得服务路径
	 * @return
	 */
	public String getServerUrl();
	/**
	 * 获取所有的Key-Value形式的文本请求参数集合。其中：
	 * <ul>
	 * <li>Key: 请求参数名</li>
	 * <li>Value: 请求参数值</li>
	 * </ul>
	 * 
	 * @return 文本请求参数集合
	 */
	public Map<String, Object> getTextParams();
	/**
	 * 获取TOP的API名称。
	 * 
	 * @return API名称
	 */
	public abstract String getApiMethodName();
	public Class<T> getResponseClass();

}
