package com.chengniu.client.pojo;

import java.util.Date;

public class BaoxianUnderwritingRequest {
	private String id;

	private String baoxianIntentId;

	private String baoxianBaseInfoId;

	private Integer userType;

	private String userId;

	private String mobile;

	private Integer status;

	private Integer failType;

	private String failMessage;

	private String cityCode;

	private String cityName;

	private Integer vehicleModelPrice;

	private String vehicleModelCode;

	private String vehicleModelName;

	private Boolean jqx;

	private Byte jqxStatus;

	private Boolean syx;

	private Byte syxStatus;

	private Boolean ccs;

	private Byte ccsStatus;

	private String szx;

	private Byte szxStatus;

	private Boolean szxBjmp;

	private Byte szxBjmpStatus;

	private String csx;

	private Byte csxStatus;

	private String ckx;

	private Byte ckxStatus;

	private String dqx;

	private Byte dqxStatus;

	private String blx;

	private Byte blxStatus;

	private String hfx;

	private Byte hfxStatus;

	private String zrx;

	private Byte zrxStatus;

	private Boolean csxBjmp;

	private Byte csxBjmpStatus;

	private Boolean ckxBjmp;

	private Byte ckxBjmpStatus;

	private Boolean ssxBjmp;

	private Byte ssxBjmpStatus;

	private Boolean dqxBjmp;

	private Byte dqxBjmpStatus;

	private Boolean blxBjmp;

	private Byte blxBjmpStatus;

	private Boolean hfxBjmp;

	private Byte hfxBjmpStatus;

	private Boolean zrxBjmp;

	private Byte zrxBjmpStatus;

	private String ssx;

	private Byte ssxStatus;

	private String sjzrx;

	private Byte sjzrxStatus;

	private Boolean sjzrxBjmp;

	private Byte sjzrxBjmpStatus;

	private Boolean tyx;

	private Byte tyxStatus;

	private Boolean zdzxc;

	private Byte zdzxcStatus;

	private Boolean jbxBjmp;

	private Byte jbxBjmpStatus;

	private Boolean xzsbssx;

	private Byte xzsbssxStatus;

	private Boolean xzsbssxBjmp;

	private Byte xzsbssxBjmpStatus;

	private Boolean cpcssx;

	private Byte cpcssxStatus;

	private Boolean zrmcty;

	private Byte zrmctyStatus;

	private Boolean ssmcty;

	private Byte ssmctyStatus;

	private String kxmpety;

	private Byte kxmpetyStatus;

	private Boolean dcsgmpty;

	private Byte dcsgmptyStatus;

	private Boolean cshwzrx;

	private Byte cshwzrxStatus;

	private Boolean cshwzrxBjmp;

	private Byte cshwzrxBjmpStatus;

	private Boolean scxlwpssx;

	private Byte scxlwpssxStatus;

	private Boolean jlcty;

	private Byte jlctyStatus;

	private Boolean jdctsssx;

	private Byte jdctsssxStatus;

	private Boolean tzckzx;

	private Byte tzckzxStatus;

	private Boolean jsshfwjzrx;

	private Byte jsshfwjzrxStatus;

	private Boolean xlqjfybcx;

	private Byte xlqjfybcxStatus;

	private Boolean jdcssbxwfzddsftyx;

	private Byte jdcssbxwfzddsftyxStatus;

	private String sgzrmpltytk;

	private Byte sgzrmpltytkStatus;

	private String dcjcdssx;

	private Byte dcjcdssxStatus;

	private Boolean bjmpx;

	private Byte bjmpxStatus;

	private Boolean fjxBjmp;

	private Byte fjxBjmpStatus;

	private Boolean fjcsryzrxBjmp;

	private Byte fjcsryzrxBjmpStatus;

	private String remark;

	private Date createTime;

	private Date updateTime;

	private Integer lastOperationNo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public String getBaoxianIntentId() {
		return baoxianIntentId;
	}

	public void setBaoxianIntentId(String baoxianIntentId) {
		this.baoxianIntentId = baoxianIntentId;
	}

	public String getBaoxianBaseInfoId() {
		return baoxianBaseInfoId;
	}

	public void setBaoxianBaseInfoId(String baoxianBaseInfoId) {
		this.baoxianBaseInfoId = baoxianBaseInfoId;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getFailType() {
		return failType;
	}

	public void setFailType(Integer failType) {
		this.failType = failType;
	}

	public String getFailMessage() {
		return failMessage;
	}

	public void setFailMessage(String failMessage) {
		this.failMessage = failMessage;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Integer getVehicleModelPrice() {
		return vehicleModelPrice;
	}

	public void setVehicleModelPrice(Integer vehicleModelPrice) {
		this.vehicleModelPrice = vehicleModelPrice;
	}

	public String getVehicleModelCode() {
		return vehicleModelCode;
	}

	public void setVehicleModelCode(String vehicleModelCode) {
		this.vehicleModelCode = vehicleModelCode;
	}

	public String getVehicleModelName() {
		return vehicleModelName;
	}

	public void setVehicleModelName(String vehicleModelName) {
		this.vehicleModelName = vehicleModelName;
	}

	public Boolean getJqx() {
		return jqx;
	}

	public void setJqx(Boolean jqx) {
		this.jqx = jqx;
	}

	public Byte getJqxStatus() {
		return jqxStatus;
	}

	public void setJqxStatus(Byte jqxStatus) {
		this.jqxStatus = jqxStatus;
	}

	public Boolean getSyx() {
		return syx;
	}

	public void setSyx(Boolean syx) {
		this.syx = syx;
	}

	public Byte getSyxStatus() {
		return syxStatus;
	}

	public void setSyxStatus(Byte syxStatus) {
		this.syxStatus = syxStatus;
	}

	public Boolean getCcs() {
		return ccs;
	}

	public void setCcs(Boolean ccs) {
		this.ccs = ccs;
	}

	public Byte getCcsStatus() {
		return ccsStatus;
	}

	public void setCcsStatus(Byte ccsStatus) {
		this.ccsStatus = ccsStatus;
	}

	public String getSzx() {
		return szx;
	}

	public void setSzx(String szx) {
		this.szx = szx == null ? null : szx.trim();
	}

	public Byte getSzxStatus() {
		return szxStatus;
	}

	public void setSzxStatus(Byte szxStatus) {
		this.szxStatus = szxStatus;
	}

	public Boolean getSzxBjmp() {
		return szxBjmp;
	}

	public void setSzxBjmp(Boolean szxBjmp) {
		this.szxBjmp = szxBjmp;
	}

	public Byte getSzxBjmpStatus() {
		return szxBjmpStatus;
	}

	public void setSzxBjmpStatus(Byte szxBjmpStatus) {
		this.szxBjmpStatus = szxBjmpStatus;
	}

	public String getCsx() {
		return csx;
	}

	public void setCsx(String csx) {
		this.csx = csx == null ? null : csx.trim();
	}

	public Byte getCsxStatus() {
		return csxStatus;
	}

	public void setCsxStatus(Byte csxStatus) {
		this.csxStatus = csxStatus;
	}

	public String getCkx() {
		return ckx;
	}

	public void setCkx(String ckx) {
		this.ckx = ckx == null ? null : ckx.trim();
	}

	public Byte getCkxStatus() {
		return ckxStatus;
	}

	public void setCkxStatus(Byte ckxStatus) {
		this.ckxStatus = ckxStatus;
	}

	public String getDqx() {
		return dqx;
	}

	public void setDqx(String dqx) {
		this.dqx = dqx == null ? null : dqx.trim();
	}

	public Byte getDqxStatus() {
		return dqxStatus;
	}

	public void setDqxStatus(Byte dqxStatus) {
		this.dqxStatus = dqxStatus;
	}

	public String getBlx() {
		return blx;
	}

	public void setBlx(String blx) {
		this.blx = blx == null ? null : blx.trim();
	}

	public Byte getBlxStatus() {
		return blxStatus;
	}

	public void setBlxStatus(Byte blxStatus) {
		this.blxStatus = blxStatus;
	}

	public String getHfx() {
		return hfx;
	}

	public void setHfx(String hfx) {
		this.hfx = hfx == null ? null : hfx.trim();
	}

	public Byte getHfxStatus() {
		return hfxStatus;
	}

	public void setHfxStatus(Byte hfxStatus) {
		this.hfxStatus = hfxStatus;
	}

	public String getZrx() {
		return zrx;
	}

	public void setZrx(String zrx) {
		this.zrx = zrx == null ? null : zrx.trim();
	}

	public Byte getZrxStatus() {
		return zrxStatus;
	}

	public void setZrxStatus(Byte zrxStatus) {
		this.zrxStatus = zrxStatus;
	}

	public Boolean getCsxBjmp() {
		return csxBjmp;
	}

	public void setCsxBjmp(Boolean csxBjmp) {
		this.csxBjmp = csxBjmp;
	}

	public Byte getCsxBjmpStatus() {
		return csxBjmpStatus;
	}

	public void setCsxBjmpStatus(Byte csxBjmpStatus) {
		this.csxBjmpStatus = csxBjmpStatus;
	}

	public Boolean getCkxBjmp() {
		return ckxBjmp;
	}

	public void setCkxBjmp(Boolean ckxBjmp) {
		this.ckxBjmp = ckxBjmp;
	}

	public Byte getCkxBjmpStatus() {
		return ckxBjmpStatus;
	}

	public void setCkxBjmpStatus(Byte ckxBjmpStatus) {
		this.ckxBjmpStatus = ckxBjmpStatus;
	}

	public Boolean getSsxBjmp() {
		return ssxBjmp;
	}

	public void setSsxBjmp(Boolean ssxBjmp) {
		this.ssxBjmp = ssxBjmp;
	}

	public Byte getSsxBjmpStatus() {
		return ssxBjmpStatus;
	}

	public void setSsxBjmpStatus(Byte ssxBjmpStatus) {
		this.ssxBjmpStatus = ssxBjmpStatus;
	}

	public Boolean getDqxBjmp() {
		return dqxBjmp;
	}

	public void setDqxBjmp(Boolean dqxBjmp) {
		this.dqxBjmp = dqxBjmp;
	}

	public Byte getDqxBjmpStatus() {
		return dqxBjmpStatus;
	}

	public void setDqxBjmpStatus(Byte dqxBjmpStatus) {
		this.dqxBjmpStatus = dqxBjmpStatus;
	}

	public Boolean getBlxBjmp() {
		return blxBjmp;
	}

	public void setBlxBjmp(Boolean blxBjmp) {
		this.blxBjmp = blxBjmp;
	}

	public Byte getBlxBjmpStatus() {
		return blxBjmpStatus;
	}

	public void setBlxBjmpStatus(Byte blxBjmpStatus) {
		this.blxBjmpStatus = blxBjmpStatus;
	}

	public Boolean getHfxBjmp() {
		return hfxBjmp;
	}

	public void setHfxBjmp(Boolean hfxBjmp) {
		this.hfxBjmp = hfxBjmp;
	}

	public Byte getHfxBjmpStatus() {
		return hfxBjmpStatus;
	}

	public void setHfxBjmpStatus(Byte hfxBjmpStatus) {
		this.hfxBjmpStatus = hfxBjmpStatus;
	}

	public Boolean getZrxBjmp() {
		return zrxBjmp;
	}

	public void setZrxBjmp(Boolean zrxBjmp) {
		this.zrxBjmp = zrxBjmp;
	}

	public Byte getZrxBjmpStatus() {
		return zrxBjmpStatus;
	}

	public void setZrxBjmpStatus(Byte zrxBjmpStatus) {
		this.zrxBjmpStatus = zrxBjmpStatus;
	}

	public String getSsx() {
		return ssx;
	}

	public void setSsx(String ssx) {
		this.ssx = ssx == null ? null : ssx.trim();
	}

	public Byte getSsxStatus() {
		return ssxStatus;
	}

	public void setSsxStatus(Byte ssxStatus) {
		this.ssxStatus = ssxStatus;
	}

	public String getSjzrx() {
		return sjzrx;
	}

	public void setSjzrx(String sjzrx) {
		this.sjzrx = sjzrx == null ? null : sjzrx.trim();
	}

	public Byte getSjzrxStatus() {
		return sjzrxStatus;
	}

	public void setSjzrxStatus(Byte sjzrxStatus) {
		this.sjzrxStatus = sjzrxStatus;
	}

	public Boolean getSjzrxBjmp() {
		return sjzrxBjmp;
	}

	public void setSjzrxBjmp(Boolean sjzrxBjmp) {
		this.sjzrxBjmp = sjzrxBjmp;
	}

	public Byte getSjzrxBjmpStatus() {
		return sjzrxBjmpStatus;
	}

	public void setSjzrxBjmpStatus(Byte sjzrxBjmpStatus) {
		this.sjzrxBjmpStatus = sjzrxBjmpStatus;
	}

	public Boolean getTyx() {
		return tyx;
	}

	public void setTyx(Boolean tyx) {
		this.tyx = tyx;
	}

	public Byte getTyxStatus() {
		return tyxStatus;
	}

	public void setTyxStatus(Byte tyxStatus) {
		this.tyxStatus = tyxStatus;
	}

	public Boolean getZdzxc() {
		return zdzxc;
	}

	public void setZdzxc(Boolean zdzxc) {
		this.zdzxc = zdzxc;
	}

	public Byte getZdzxcStatus() {
		return zdzxcStatus;
	}

	public void setZdzxcStatus(Byte zdzxcStatus) {
		this.zdzxcStatus = zdzxcStatus;
	}

	public Boolean getJbxBjmp() {
		return jbxBjmp;
	}

	public void setJbxBjmp(Boolean jbxBjmp) {
		this.jbxBjmp = jbxBjmp;
	}

	public Byte getJbxBjmpStatus() {
		return jbxBjmpStatus;
	}

	public void setJbxBjmpStatus(Byte jbxBjmpStatus) {
		this.jbxBjmpStatus = jbxBjmpStatus;
	}

	public Boolean getXzsbssx() {
		return xzsbssx;
	}

	public void setXzsbssx(Boolean xzsbssx) {
		this.xzsbssx = xzsbssx;
	}

	public Byte getXzsbssxStatus() {
		return xzsbssxStatus;
	}

	public void setXzsbssxStatus(Byte xzsbssxStatus) {
		this.xzsbssxStatus = xzsbssxStatus;
	}

	public Boolean getXzsbssxBjmp() {
		return xzsbssxBjmp;
	}

	public void setXzsbssxBjmp(Boolean xzsbssxBjmp) {
		this.xzsbssxBjmp = xzsbssxBjmp;
	}

	public Byte getXzsbssxBjmpStatus() {
		return xzsbssxBjmpStatus;
	}

	public void setXzsbssxBjmpStatus(Byte xzsbssxBjmpStatus) {
		this.xzsbssxBjmpStatus = xzsbssxBjmpStatus;
	}

	public Boolean getCpcssx() {
		return cpcssx;
	}

	public void setCpcssx(Boolean cpcssx) {
		this.cpcssx = cpcssx;
	}

	public Byte getCpcssxStatus() {
		return cpcssxStatus;
	}

	public void setCpcssxStatus(Byte cpcssxStatus) {
		this.cpcssxStatus = cpcssxStatus;
	}

	public Boolean getZrmcty() {
		return zrmcty;
	}

	public void setZrmcty(Boolean zrmcty) {
		this.zrmcty = zrmcty;
	}

	public Byte getZrmctyStatus() {
		return zrmctyStatus;
	}

	public void setZrmctyStatus(Byte zrmctyStatus) {
		this.zrmctyStatus = zrmctyStatus;
	}

	public Boolean getSsmcty() {
		return ssmcty;
	}

	public void setSsmcty(Boolean ssmcty) {
		this.ssmcty = ssmcty;
	}

	public Byte getSsmctyStatus() {
		return ssmctyStatus;
	}

	public void setSsmctyStatus(Byte ssmctyStatus) {
		this.ssmctyStatus = ssmctyStatus;
	}

	public String getKxmpety() {
		return kxmpety;
	}

	public void setKxmpety(String kxmpety) {
		this.kxmpety = kxmpety == null ? null : kxmpety.trim();
	}

	public Byte getKxmpetyStatus() {
		return kxmpetyStatus;
	}

	public void setKxmpetyStatus(Byte kxmpetyStatus) {
		this.kxmpetyStatus = kxmpetyStatus;
	}

	public Boolean getDcsgmpty() {
		return dcsgmpty;
	}

	public void setDcsgmpty(Boolean dcsgmpty) {
		this.dcsgmpty = dcsgmpty;
	}

	public Byte getDcsgmptyStatus() {
		return dcsgmptyStatus;
	}

	public void setDcsgmptyStatus(Byte dcsgmptyStatus) {
		this.dcsgmptyStatus = dcsgmptyStatus;
	}

	public Boolean getCshwzrx() {
		return cshwzrx;
	}

	public void setCshwzrx(Boolean cshwzrx) {
		this.cshwzrx = cshwzrx;
	}

	public Byte getCshwzrxStatus() {
		return cshwzrxStatus;
	}

	public void setCshwzrxStatus(Byte cshwzrxStatus) {
		this.cshwzrxStatus = cshwzrxStatus;
	}

	public Boolean getCshwzrxBjmp() {
		return cshwzrxBjmp;
	}

	public void setCshwzrxBjmp(Boolean cshwzrxBjmp) {
		this.cshwzrxBjmp = cshwzrxBjmp;
	}

	public Byte getCshwzrxBjmpStatus() {
		return cshwzrxBjmpStatus;
	}

	public void setCshwzrxBjmpStatus(Byte cshwzrxBjmpStatus) {
		this.cshwzrxBjmpStatus = cshwzrxBjmpStatus;
	}

	public Boolean getScxlwpssx() {
		return scxlwpssx;
	}

	public void setScxlwpssx(Boolean scxlwpssx) {
		this.scxlwpssx = scxlwpssx;
	}

	public Byte getScxlwpssxStatus() {
		return scxlwpssxStatus;
	}

	public void setScxlwpssxStatus(Byte scxlwpssxStatus) {
		this.scxlwpssxStatus = scxlwpssxStatus;
	}

	public Boolean getJlcty() {
		return jlcty;
	}

	public void setJlcty(Boolean jlcty) {
		this.jlcty = jlcty;
	}

	public Byte getJlctyStatus() {
		return jlctyStatus;
	}

	public void setJlctyStatus(Byte jlctyStatus) {
		this.jlctyStatus = jlctyStatus;
	}

	public Boolean getJdctsssx() {
		return jdctsssx;
	}

	public void setJdctsssx(Boolean jdctsssx) {
		this.jdctsssx = jdctsssx;
	}

	public Byte getJdctsssxStatus() {
		return jdctsssxStatus;
	}

	public void setJdctsssxStatus(Byte jdctsssxStatus) {
		this.jdctsssxStatus = jdctsssxStatus;
	}

	public Boolean getTzckzx() {
		return tzckzx;
	}

	public void setTzckzx(Boolean tzckzx) {
		this.tzckzx = tzckzx;
	}

	public Byte getTzckzxStatus() {
		return tzckzxStatus;
	}

	public void setTzckzxStatus(Byte tzckzxStatus) {
		this.tzckzxStatus = tzckzxStatus;
	}

	public Boolean getJsshfwjzrx() {
		return jsshfwjzrx;
	}

	public void setJsshfwjzrx(Boolean jsshfwjzrx) {
		this.jsshfwjzrx = jsshfwjzrx;
	}

	public Byte getJsshfwjzrxStatus() {
		return jsshfwjzrxStatus;
	}

	public void setJsshfwjzrxStatus(Byte jsshfwjzrxStatus) {
		this.jsshfwjzrxStatus = jsshfwjzrxStatus;
	}

	public Boolean getXlqjfybcx() {
		return xlqjfybcx;
	}

	public void setXlqjfybcx(Boolean xlqjfybcx) {
		this.xlqjfybcx = xlqjfybcx;
	}

	public Byte getXlqjfybcxStatus() {
		return xlqjfybcxStatus;
	}

	public void setXlqjfybcxStatus(Byte xlqjfybcxStatus) {
		this.xlqjfybcxStatus = xlqjfybcxStatus;
	}

	public Boolean getJdcssbxwfzddsftyx() {
		return jdcssbxwfzddsftyx;
	}

	public void setJdcssbxwfzddsftyx(Boolean jdcssbxwfzddsftyx) {
		this.jdcssbxwfzddsftyx = jdcssbxwfzddsftyx;
	}

	public Byte getJdcssbxwfzddsftyxStatus() {
		return jdcssbxwfzddsftyxStatus;
	}

	public void setJdcssbxwfzddsftyxStatus(Byte jdcssbxwfzddsftyxStatus) {
		this.jdcssbxwfzddsftyxStatus = jdcssbxwfzddsftyxStatus;
	}

	public String getSgzrmpltytk() {
		return sgzrmpltytk;
	}

	public void setSgzrmpltytk(String sgzrmpltytk) {
		this.sgzrmpltytk = sgzrmpltytk == null ? null : sgzrmpltytk.trim();
	}

	public Byte getSgzrmpltytkStatus() {
		return sgzrmpltytkStatus;
	}

	public void setSgzrmpltytkStatus(Byte sgzrmpltytkStatus) {
		this.sgzrmpltytkStatus = sgzrmpltytkStatus;
	}

	public String getDcjcdssx() {
		return dcjcdssx;
	}

	public void setDcjcdssx(String dcjcdssx) {
		this.dcjcdssx = dcjcdssx == null ? null : dcjcdssx.trim();
	}

	public Byte getDcjcdssxStatus() {
		return dcjcdssxStatus;
	}

	public void setDcjcdssxStatus(Byte dcjcdssxStatus) {
		this.dcjcdssxStatus = dcjcdssxStatus;
	}

	public Boolean getBjmpx() {
		return bjmpx;
	}

	public void setBjmpx(Boolean bjmpx) {
		this.bjmpx = bjmpx;
	}

	public Byte getBjmpxStatus() {
		return bjmpxStatus;
	}

	public void setBjmpxStatus(Byte bjmpxStatus) {
		this.bjmpxStatus = bjmpxStatus;
	}

	public Boolean getFjxBjmp() {
		return fjxBjmp;
	}

	public void setFjxBjmp(Boolean fjxBjmp) {
		this.fjxBjmp = fjxBjmp;
	}

	public Byte getFjxBjmpStatus() {
		return fjxBjmpStatus;
	}

	public void setFjxBjmpStatus(Byte fjxBjmpStatus) {
		this.fjxBjmpStatus = fjxBjmpStatus;
	}

	public Boolean getFjcsryzrxBjmp() {
		return fjcsryzrxBjmp;
	}

	public void setFjcsryzrxBjmp(Boolean fjcsryzrxBjmp) {
		this.fjcsryzrxBjmp = fjcsryzrxBjmp;
	}

	public Byte getFjcsryzrxBjmpStatus() {
		return fjcsryzrxBjmpStatus;
	}

	public void setFjcsryzrxBjmpStatus(Byte fjcsryzrxBjmpStatus) {
		this.fjcsryzrxBjmpStatus = fjcsryzrxBjmpStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getLastOperationNo() {
		return lastOperationNo;
	}

	public void setLastOperationNo(Integer lastOperationNo) {
		this.lastOperationNo = lastOperationNo;
	}

}