package com.chengniu.client.pojo;

public class BaoxianCompanyVO extends BaoxianCompany {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6756493514915462804L;

	/**
	 * 开通状态
	 */
	public enum OPEN_TYPE {
		CLOSED(-1), OPENED_C(0), OPENED_B(1), OPENED_ALL(2);
		public int value;

		OPEN_TYPE(int type) {
			this.value = type;
		}
	}

	private Integer userType;

	private Boolean open;

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}
}