package com.chengniu.client.pojo;

import java.io.Serializable;

/**
 * 缓存中保存的投保城市对象 2015-11-05
 *
 * @author zhongjie
 */
public class OpenedCityBO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3030896103775664545L;
	public BaoxianCityFanhua city;
	public boolean isOpenedC = false;
	public boolean isOpenedB = false;
}
