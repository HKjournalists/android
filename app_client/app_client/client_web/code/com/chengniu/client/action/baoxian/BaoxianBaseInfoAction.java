package com.chengniu.client.action.baoxian;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.util.StringUtils;

import com.chengniu.client.action.SuperAction;
import com.chengniu.client.common.CommonUtil;
import com.chengniu.client.common.SerializableUtils;
import com.chengniu.client.pojo.BaoxianBaseInfo;
import com.chengniu.client.pojo.BaoxianCompany;
import com.chengniu.client.pojo.BaoxianIntent;
import com.chengniu.client.pojo.BaoxianPeisong;
import com.chengniu.client.pojo.BaoxianReportPrice;
import com.chengniu.client.pojo.SearchVO;
import com.chengniu.client.service.BaoxianBaseInfoService;
import com.chengniu.client.service.BaoxianCompanyService;
import com.chengniu.client.service.BaoxianInsureInfoService;
import com.chengniu.client.service.BaoxianReportPriceService;
import com.chengniu.client.service.BaoxianUnderwritingReportService;
import com.chengniu.client.service.BaoxianUnderwritingService;
import com.chengniu.client.service.impl.BaoxianWorkflowController;
import com.chengniu.client.service.FanhuaService;
import com.google.gson.GsonBuilder;
import com.kplus.orders.rpc.dto.PageVO;

public class BaoxianBaseInfoAction extends SuperAction {
	private static final long serialVersionUID = -7573690539208123981L;

	@Resource(name = "baoxianBaseInfoService")
	public BaoxianBaseInfoService baoxianBaseInfoService;
	@Resource(name = "baoxianCompanyService")
	public BaoxianCompanyService baoxianCompanyService;
	@Resource(name = "baoxianInsureInfoService")
	public BaoxianInsureInfoService baoxianInsureInfoService;
	@Resource(name = "baoxianUnderwritingReportService")
	public BaoxianUnderwritingReportService baoxianUnderwritingReportService;
	@Resource(name = "baoxianUnderwritingService")
	public BaoxianUnderwritingService baoxianUnderwritingService;
	@Resource(name = "baoxianReportPriceService")
	public BaoxianReportPriceService baoxianReportPriceService;

	public String id;
	public String vehicleNum;
	public String cityCode;
	public String drivingUrl;
	public String guohuDate;
	public String vehicleModelCode;
	public String price;
	public String idCardName;
	public String mobile;
	public String cityName;
	public String idCardNum;
	public String vehicleModelName;
	public String idCardType;
	public String guohu;
	public String idCardUrl;

	public String queryUserInfo() throws Exception {
		return super.ajax(baoxianBaseInfoService.query(id,
				getCurrentOperator().getUserId(),
				getCurrentOperator().getUserType()));
	}

	public String listUserInfo() throws Exception {
		SearchVO vo = this.getSearch();
		vo.setUserId(this.getCurrentOperator().getUserId());
		vo.setUserType(this.getCurrentOperator().getUserType());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		PageVO<?> page = this.baoxianBaseInfoService.page(vo);
		if (page != null) {
			resultMap.put("list", page.getEntityList());
			resultMap.put("total", page.getCountRecords());
		} else
			resultMap.put("total", 0);
		return super.ajax(resultMap);
	}

	public String saveUserInfo() throws Exception {
		BaoxianBaseInfo info = new GsonBuilder().setDateFormat("yyyy-MM-dd")
				.disableHtmlEscaping().create()
				.fromJson(this.getClientParam(), BaoxianBaseInfo.class);
		info.setUserType(this.getCurrentOperator().getUserType());
		info.setUserId(this.getCurrentOperator().getUserId());

		BaoxianWorkflowController controller = new BaoxianWorkflowController();
		BaoxianIntent ret = controller.submitInfoAndCreateIntent(info);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", ret != null);
		resultMap.put("id", ret.getId());
		return super.ajax(resultMap);
	}

	@Deprecated
	public String fastReport() throws Exception {
		BaoxianBaseInfo info = new BaoxianBaseInfo();
		info.setCityCode(cityCode);
		info.setToubaoren(true);
		info.setDrivingUrl(drivingUrl);
		info.setId(id);
		info.setUserId(this.getCurrentOperator().getUserId());
		info.setVehicleNum(vehicleNum);
		info.setVehicleModelCode(vehicleModelCode);
		info.setBeibaoren(true);
		info.setCityName(cityName);
		info.setVehicleModelName(vehicleModelName);
		info.setUserType(this.getCurrentOperator().getUserType());
		info.setVehicleModelPrice(price);
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, BaoxianReportPrice> resultMap = new HashMap<String, BaoxianReportPrice>();
		if (this.baoxianBaseInfoService.save(info)) {
			map.put("id", info.getId());
			List<BaoxianReportPrice> list = baoxianReportPriceService
					.queryPrice(info.getCityCode(), price);
			List<BaoxianCompany> companyList = this.baoxianCompanyService
					.list(info.getCityCode(), this.getCurrentOperator()
							.getUserType());
			List<BaoxianReportPrice> priceList = new ArrayList<BaoxianReportPrice>(
					companyList.size() * 3);
			BaoxianReportPrice s0 = null;
			BaoxianReportPrice s = null;
			BaoxianReportPrice ss = null;
			for (BaoxianReportPrice price : list) {
				if (s0 == null && price.getType().equals("0"))
					s0 = price;
				else if (s == null && price.getType().equals("1"))
					s = price;
				else if (ss == null && price.getType().equals("2"))
					ss = price;
				resultMap.put(
						price.getBaoxianCompanyCode() + ":" + price.getType(),
						price);
			}
			for (BaoxianCompany company : companyList) {
				for (int type = 0; type < 3; type++) {
					try {
						if (resultMap.get(company.getCode() + ":" + type) != null) {
							priceList.add(resultMap.get(company.getCode() + ":"
									+ type));
						} else {
							BaoxianReportPrice sp;
							if (type == 0) {
								sp = s0;
							} else if (type == 1)
								sp = s;
							else
								sp = ss;
							BaoxianReportPrice bs = CommonUtil.simpleValueCopy(
									sp, BaoxianReportPrice.class);
							bs.setBaoxianCompanyCode(company.getCode());
							bs.setId(SerializableUtils.allocUUID());
							bs.setBaoxianCompanyName(company.getName());
							bs.setType(String.valueOf(type));
							priceList.add(bs);
						}
					} catch (Exception e) {
					}
				}
			}
			map.put("list", priceList);
			map.put("total", priceList != null ? priceList.size() : 0);
		}
		return super.ajax(map);
	}

	public String fixUserInfo() throws Exception {
		BaoxianBaseInfo info = new BaoxianBaseInfo();
		info.setMobile(mobile);
		info.setIdCardName(idCardName);
		info.setIdCardNum(idCardNum);
		info.setIdCardUrl(idCardUrl);
		try {
			info.setIdCardType(Integer.parseInt(idCardType));
		} catch (Exception e) {
			info.setIdCardType(1);
		}

		if ("0".equals(guohu)) {
			info.setGuohu(false);
		} else {
			info.setGuohu(true);
		}

		if (info.getGuohu()) {
			info.setGuohuDate(guohuDate);
		}
		info.setUserId(this.getCurrentOperator().getUserId());
		info.setUserType(this.getCurrentOperator().getUserType());

		BaoxianWorkflowController controller = new BaoxianWorkflowController();
		boolean ret = controller.updateUserInfo(info);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", ret);
		return super.ajax(map);
	}

	public String deleteUserInfo() throws Exception {
		BaoxianBaseInfo info = new BaoxianBaseInfo();
		info.setId(id);
		info.setUserId(this.getCurrentOperator().getUserId());
		info.setUserType(this.getCurrentOperator().getUserType());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", this.baoxianBaseInfoService.delete(info));
		map.put("id", info.getId());
		return super.ajax(map);
	}

	public String queryPeisong() throws Exception {
		return super.ajax(this.baoxianInsureInfoService.queryPeisong(id, this
				.getCurrentOperator().getUserId(), this.getCurrentOperator()
				.getUserType()));
	}

	public String listPeisong() throws Exception {
		SearchVO vo = this.getSearch();
		vo.setUserId(this.getCurrentOperator().getUserId());
		vo.setUserType(this.getCurrentOperator().getUserType());
		Map<String, Object> map = new HashMap<String, Object>();
		PageVO<BaoxianPeisong> page = this.baoxianInsureInfoService
				.pagePeisong(vo);
		if (page != null) {
			map.put("list", page.getEntityList());
			map.put("total", page.getCountRecords());
		} else {
			map.put("total", 0);
		}
		return super.ajax(map);
	}

	public String deletePeisong() throws Exception {
		boolean rs = true;
		if (StringUtils.hasText(id)) {
			String[] ids = id.split(",");
			for (String tp : ids) {
				BaoxianPeisong ps = new BaoxianPeisong();
				ps.setUserId(this.getCurrentOperator().getUserId());
				ps.setId(tp);
				ps.setUserType(this.getCurrentOperator().getUserType());
				if (!this.baoxianInsureInfoService.deletePeisong(ps)) {
					rs = false;
				}
			}
		}
		return super.ajax(rs);
	}


	public void setId(String id) {
		this.id = id;
	}

	public void setVehicleNum(String vehicleNum) {
		this.vehicleNum = vehicleNum;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public void setDrivingUrl(String drivingUrl) {
		this.drivingUrl = drivingUrl;
	}

	public void setGuohuDate(String guohuDate) {
		this.guohuDate = guohuDate;
	}

	public void setVehicleModelCode(String vehicleModelCode) {
		this.vehicleModelCode = vehicleModelCode;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public void setIdCardName(String idCardName) {
		this.idCardName = idCardName;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public void setIdCardNum(String idCardNum) {
		this.idCardNum = idCardNum;
	}

	public void setVehicleModelName(String vehicleModelName) {
		this.vehicleModelName = vehicleModelName;
	}

	public void setIdCardType(String idCardType) {
		this.idCardType = idCardType;
	}

	public void setGuohu(String guohu) {
		this.guohu = guohu;
	}

	public void setIdCardUrl(String idCardUrl) {
		this.idCardUrl = idCardUrl;
	}

}