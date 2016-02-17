package com.chengniu.client.pojo;

import java.io.Serializable;
import java.util.Date;

public class BaoxianCompany implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5641377907739167350L;

	private String id;

	private String code;

	private String cityCode;

	private String name;

	private String companyName;

	private String pic;

	private String baiduName;

	private String remark;

	private Integer openInfo;

	private String province;

	private Float maxRebate;

	private Date createTime;
    private Boolean supportAutoQuote;
	private Boolean supportAutomatic;
    private Boolean syncFlag;
	private Date updateTime;
	private String picSmall;
	private Long createBy;

	private Long updateBy;
	private String channel;
    private Boolean channelStatus;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code == null ? null : code.trim();
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode == null ? null : cityCode.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName == null ? null : companyName.trim();
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic == null ? null : pic.trim();
	}

	public String getBaiduName() {
		return baiduName;
	}

	public void setBaiduName(String baiduName) {
		this.baiduName = baiduName == null ? null : baiduName.trim();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public Integer getOpenInfo() {
		return openInfo;
	}

	public void setOpenInfo(Integer openInfo) {
		this.openInfo = openInfo;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province == null ? null : province.trim();
	}

	public Float getMaxRebate() {
		return maxRebate;
	}

	public void setMaxRebate(Float maxRebate) {
		this.maxRebate = maxRebate;
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

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Boolean getSupportAutomatic() {
		return supportAutomatic;
	}

	public void setSupportAutomatic(Boolean supportAutomatic) {
		this.supportAutomatic = supportAutomatic;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getPicSmall() {
		if (picSmall == null)
			picSmall = pic;
		return picSmall;
	}

	public void setPicSmall(String picSmall) {
		this.picSmall = picSmall;
	}

    public Boolean getSupportAutoQuote() {
        return supportAutoQuote;
    }

    public void setSupportAutoQuote(Boolean supportAutoQuote) {
        this.supportAutoQuote = supportAutoQuote;
    }

    public Boolean getSyncFlag() {
        return syncFlag;
    }

    public void setSyncFlag(Boolean syncFlag) {
        this.syncFlag = syncFlag;
    }

    public Boolean getChannelStatus() {
        return channelStatus;
    }

    public void setChannelStatus(Boolean channelStatus) {
        this.channelStatus = channelStatus;
    }
}