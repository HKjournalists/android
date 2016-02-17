package com.chengniu.client.common;

import java.util.List;
import java.util.Map;

import com.chengniu.client.pojo.BaoxianBaseInfoMedia;
import com.chengniu.client.pojo.BaoxianBaseInfoMediaDTO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 保险影像资料字典
 */
public final class MediaDictionary {
	public static final String IDCardCode = "CAROWNERID1";
	public static final String DrivingLicenseCode = "DRIVINGLICENSE1";
	public static String AgencyCode = "CAROWNERID3";

	public static final Integer GROUP_OWNER = 0;
	public static final Integer GROUP_CAR = 1;
	public static final Integer GROUP_CERT = 2;
	public static final Integer GROUP_OTHER = 3;

	public final static Map<String, Media> sMediaMap = Maps.newHashMap();

	static {
		// 车主证件
		Media media = new Media(
				"CAROWNERID1",
				"车主身份证正面照",
				GROUP_OWNER,
				0,
				"http://img.chengniu.com/baoxian/templateid/CAROWNERID1_DEMO.jpg",
				"车主身份证正面照");
		sMediaMap.put(media.getCode(), media);
		media = new Media(
				"CAROWNERID2",
				"车主身份证反面照",
				GROUP_OWNER,
				1,
				"http://img.chengniu.com/baoxian/templateid/CAROWNERID2_DEMO.jpg",
				"车主身份证反面照");
		sMediaMap.put(media.getCode(), media);
		media = new Media(
				"CAROWNERID3",
				"车主组织机构代码证照",
				GROUP_OWNER,
				2,
				"http://img.chengniu.com/baoxian/templateid/CAROWNERID3_DEMO.jpg",
				"车主组织机构代码证照");
		sMediaMap.put(media.getCode(), media);

		// 投保人证件
		media = new Media(
				"APPLICANTID1",
				"投保人身份证正面照",
				GROUP_OWNER,
				10,
				"http://img.chengniu.com/baoxian/templateid/APPLICANTID1_DEMO.jpg",
				"投保人身份证正面照");
		sMediaMap.put(media.getCode(), media);
		media = new Media(
				"APPLICANTID2",
				"投保人身份证反面照",
				GROUP_OWNER,
				11,
				"http://img.chengniu.com/baoxian/templateid/APPLICANTID2_DEMO.jpg",
				"投保人身份证反面照");
		sMediaMap.put(media.getCode(), media);
		media = new Media(
				"APPLICANTID3",
				"投保人组织机构代码证",
				GROUP_OWNER,
				12,
				"http://img.chengniu.com/baoxian/templateid/APPLICANTID3_DEMO.jpg",
				"投保人组织机构代码证");
		sMediaMap.put(media.getCode(), media);

		// 被保人证件
		media = new Media(
				"INSUREDID1",
				"被保人身份证正面",
				GROUP_OWNER,
				20,
				"http://img.chengniu.com/baoxian/templateid/INSUREDID1_DEMO.jpg",
				"被保人身份证正面");
		sMediaMap.put(media.getCode(), media);
		media = new Media(
				"INSUREDID2",
				"被保人身份证反面",
				GROUP_OWNER,
				21,
				"http://img.chengniu.com/baoxian/templateid/INSUREDID2_DEMO.jpg",
				"被保人身份证反面");
		sMediaMap.put(media.getCode(), media);
		media = new Media(
				"INSUREDID3",
				"被保人机构代码证",
				GROUP_OWNER,
				22,
				"http://img.chengniu.com/baoxian/templateid/INSUREDID3_DEMO.jpg",
				"被保人组织机构代码证照");
		sMediaMap.put(media.getCode(), media);

		// 驾驶证
		media = new Media(
				"DRIVERLICENSE1",
				"驾驶证正页照",
				GROUP_OWNER,
				30,
				"http://img.chengniu.com/baoxian/templateid/DRIVERLICENSE1_DEMO.jpg",
				"驾驶证正页照");
		sMediaMap.put(media.getCode(), media);
		media = new Media(
				"DRIVERLICENSE2",
				"驾驶证副页照",
				GROUP_OWNER,
				31,
				"http://img.chengniu.com/baoxian/templateid/DRIVERLICENSE2_DEMO.jpg",
				"驾驶证副页照");
		sMediaMap.put(media.getCode(), media);

		// 暂住证
		media = new Media(
				"TEMPLIVECERT",
				"暂住证",
				GROUP_OWNER,
				40,
				"http://img.chengniu.com/baoxian/templateid/TEMPLIVECERT_DEMO.jpg",
				"暂住证");
		sMediaMap.put(media.getCode(), media);

		// 车辆相关证件
		media = new Media(
				"DRIVINGLICENSE1",
				"行驶证正页照",
				GROUP_CAR,
				0,
				"http://img.chengniu.com/baoxian/templateid/DRIVINGLICENSE1_DEMO.jpg",
				"行驶证正页照");
		sMediaMap.put(media.getCode(), media);
		media = new Media(
				"DRIVINGLICENSE2",
				"行驶证副页照",
				GROUP_CAR,
				1,
				"http://img.chengniu.com/baoxian/templateid/DRIVINGLICENSE2_DEMO.jpg",
				"行驶证副页照");
		sMediaMap.put(media.getCode(), media);
		media = new Media(
				"DRIVINGLICENSE3",
				"行驶证年审页照",
				GROUP_CAR,
				2,
				"http://img.chengniu.com/baoxian/templateid/DRIVINGLICENSE3_DEMO.jpg",
				"行驶证年审页照");
		sMediaMap.put(media.getCode(), media);

		media = new Media(
				"LASTPOLICYNO",
				"上年保单照",
				GROUP_CAR,
				20,
				"http://img.chengniu.com/baoxian/templateid/LASTPOLICYNO_DEMO.jpg",
				"上年保单照");
		sMediaMap.put(media.getCode(), media);
		media = new Media("TAXCERT", "完税凭证", GROUP_CAR, 21,
				"http://img.chengniu.com/baoxian/templateid/TAXCERT_DEMO.jpg",
				"完税凭证");
		sMediaMap.put(media.getCode(), media);
		media = new Media(
				"APPNTCONFIRM",
				"投保确认书",
				GROUP_CAR,
				22,
				"http://img.chengniu.com/baoxian/templateid/APPNTCONFIRM_DEMO.jpg",
				"投保确认书");
		sMediaMap.put(media.getCode(), media);
		media = new Media(
				"LOCALCONFIRM",
				"本地确认书",
				GROUP_CAR,
				23,
				"http://img.chengniu.com/baoxian/templateid/LOCALCONFIRM_DEMO.jpg",
				"本地确认书");
		sMediaMap.put(media.getCode(), media);
		media = new Media(
				"SECONDINVOICE",
				"二手车交易发票",
				GROUP_CAR,
				24,
				"http://img.chengniu.com/baoxian/templateid/SECONDINVOICE_DEMO.jpg",
				"二手车交易发票");
		sMediaMap.put(media.getCode(), media);
		media = new Media(
				"LASTCLAIMS",
				"上年理赔截图",
				GROUP_CAR,
				25,
				"http://img.chengniu.com/baoxian/templateid/LASTCLAIMS_DEMO.jpg",
				"上年理赔截图");
		sMediaMap.put(media.getCode(), media);
		media = new Media(
				"CARREGISTER",
				"车辆登记证",
				GROUP_CAR,
				26,
				"http://img.chengniu.com/baoxian/templateid/CARREGISTER_DEMO.jpg",
				"车辆登记证");
		sMediaMap.put(media.getCode(), media);

		// 验车照
		media = new Media("CARF", "车辆正面照片", GROUP_CERT, 0,
				"http://img.chengniu.com/baoxian/templateid/CARF_DEMO.jpg",
				"车辆正面照片");
		sMediaMap.put(media.getCode(), media);
		media = new Media("CARB", "车辆正后照片", GROUP_CERT, 1,
				"http://img.chengniu.com/baoxian/templateid/CARB_DEMO.jpg",
				"车辆正后照片");
		sMediaMap.put(media.getCode(), media);
		media = new Media("CARFL45D", "车辆前左45度照片", GROUP_CERT, 2,
				"http://img.chengniu.com/baoxian/templateid/CARFL45D_DEMO.jpg",
				"车辆前左45度照片");
		sMediaMap.put(media.getCode(), media);
		media = new Media("CARFR45D", "车辆前右45度照片", GROUP_CERT, 3,
				"http://img.chengniu.com/baoxian/templateid/CARFR45D_DEMO.jpg",
				"车辆前右45度照片");
		sMediaMap.put(media.getCode(), media);
		media = new Media("CARBL45D", "车辆后左45度照片", GROUP_CERT, 4,
				"http://img.chengniu.com/baoxian/templateid/CARBL45D_DEMO.jpg",
				"车辆后左45度照片");
		sMediaMap.put(media.getCode(), media);
		media = new Media("CARBR45D", "车辆后右45度照片", GROUP_CERT, 5,
				"http://img.chengniu.com/baoxian/templateid/CARBR45D_DEMO.jpg",
				"车辆后右45度照片");
		sMediaMap.put(media.getCode(), media);
		media = new Media(
				"CARFRAMENO",
				"带车架号照片",
				GROUP_CERT,
				6,
				"http://img.chengniu.com/baoxian/templateid/CARFRAMENO_DEMO.jpg",
				"带车架号照片");
		sMediaMap.put(media.getCode(), media);
		media = new Media(
				"CARANDPEOPLE",
				"人车合影",
				GROUP_CERT,
				7,
				"http://img.chengniu.com/baoxian/templateid/CARANDPEOPLE_DEMO.jpg",
				"人车合影");
		sMediaMap.put(media.getCode(), media);

		// 车大灯
		media = new Media(
				"BIGLIGHT1",
				"所有大灯特写一",
				GROUP_CERT,
				10,
				"http://img.chengniu.com/baoxian/templateid/BIGLIGHT1_DEMO.jpg",
				"所有大灯特写一");
		sMediaMap.put(media.getCode(), media);
		media = new Media(
				"BIGLIGHT2",
				"所有大灯特写二",
				GROUP_CERT,
				11,
				"http://img.chengniu.com/baoxian/templateid/BIGLIGHT2_DEMO.jpg",
				"所有大灯特写二");
		sMediaMap.put(media.getCode(), media);
		media = new Media(
				"BIGLIGHT3",
				"所有大灯特写三",
				GROUP_CERT,
				12,
				"http://img.chengniu.com/baoxian/templateid/BIGLIGHT3_DEMO.jpg",
				"所有大灯特写三");
		sMediaMap.put(media.getCode(), media);
		media = new Media(
				"BIGLIGHT4",
				"所有大灯特写四",
				GROUP_CERT,
				13,
				"http://img.chengniu.com/baoxian/templateid/BIGLIGHT4_DEMO.jpg",
				"所有大灯特写四");
		sMediaMap.put(media.getCode(), media);

		// 倒车镜
		media = new Media(
				"BACKMIRROF",
				"倒车镜前",
				GROUP_CERT,
				20,
				"http://img.chengniu.com/baoxian/templateid/BACKMIRROF_DEMO.jpg",
				"倒车镜前");
		sMediaMap.put(media.getCode(), media);
		media = new Media(
				"BACKMIRROB",
				"倒车镜后",
				GROUP_CERT,
				21,
				"http://img.chengniu.com/baoxian/templateid/BACKMIRROB_DEMO.jpg",
				"倒车镜后");
		sMediaMap.put(media.getCode(), media);

		media = new Media(
				"NAMEPLATE",
				"铭牌",
				GROUP_CERT,
				30,
				"http://img.chengniu.com/baoxian/templateid/NAMEPLATE_DEMO.jpg",
				"铭牌");
		sMediaMap.put(media.getCode(), media);
		media = new Media(
				"ENGINEBAY",
				"发动机舱",
				GROUP_CERT,
				31,
				"http://img.chengniu.com/baoxian/templateid/ENGINEBAY_DEMO.jpg",
				"发动机舱");
		sMediaMap.put(media.getCode(), media);
		media = new Media(
				"DASHBOARDL",
				"左车仪表盘内饰",
				GROUP_CERT,
				32,
				"http://img.chengniu.com/baoxian/templateid/DASHBOARDL_DEMO.jpg",
				"左车仪表盘内饰");
		sMediaMap.put(media.getCode(), media);
		media = new Media(
				"DASHBOARDR",
				"右车仪表盘内饰",
				GROUP_CERT,
				33,
				"http://img.chengniu.com/baoxian/templateid/DASHBOARDR_DEMO.jpg",
				"右车仪表盘内饰");
		sMediaMap.put(media.getCode(), media);
		media = new Media("GLASSF", "前挡风玻璃", GROUP_CERT, 34,
				"http://img.chengniu.com/baoxian/templateid/GLASSF_DEMO.jpg",
				"前挡风玻璃");
		sMediaMap.put(media.getCode(), media);
		media = new Media("GLASSID", "玻璃标识", GROUP_CERT, 35,
				"http://img.chengniu.com/baoxian/templateid/GLASSID_DEMO.jpg",
				"玻璃标识");
		sMediaMap.put(media.getCode(), media);
		media = new Media(
				"DASHBOARD",
				"打着火拍仪表盘",
				GROUP_CERT,
				36,
				"http://img.chengniu.com/baoxian/templateid/DASHBOARD_DEMO.jpg",
				"打着火拍仪表盘");
		sMediaMap.put(media.getCode(), media);

		// 文件信息
		media = new Media(
				"TAXREGISTER",
				"税务登记证",
				GROUP_OTHER,
				0,
				"http://img.chengniu.com/baoxian/templateid/TAXREGISTER_DEMO.jpg",
				"税务登记证");
		sMediaMap.put(media.getCode(), media);
		media = new Media(
				"INFONOTIFY",
				"纳税人、扣缴义务人涉税保密信息告知书",
				GROUP_OTHER,
				1,
				"http://img.chengniu.com/baoxian/templateid/INFONOTIFY_DEMO.jpg",
				"纳税人、扣缴义务人涉税保密信息告知书");
		sMediaMap.put(media.getCode(), media);
		media = new Media(
				"REMITCERT",
				"纳税人、减免税证明",
				GROUP_OTHER,
				2,
				"http://img.chengniu.com/baoxian/templateid/REMITCERT_DEMO.jpg",
				"纳税人、减免税证明");
		sMediaMap.put(media.getCode(), media);

		// 其它
		media = new Media(
				"LASTBIZPOLICYNO",
				"上年商业险保单照",
				GROUP_OTHER,
				10,
				"http://img.chengniu.com/baoxian/templateid/LASTBIZPOLICYNO_DEMO.jpg",
				"上年商业险保单照");
		sMediaMap.put(media.getCode(), media);
		media = new Media(
				"LASTCIPOLICYNO",
				"上年交强险保单照",
				GROUP_OTHER,
				11,
				"http://img.chengniu.com/baoxian/templateid/LASTCIPOLICYNO_DEMO.jpg",
				"上年交强险保单照");
		sMediaMap.put(media.getCode(), media);

	}

	public final static Media findByCode(String code) {
		return sMediaMap.get(code);
	}

	public static final class Media {
		String code;
		String name;
		Integer type;
		int weight;
		String template;
		String description;

		public Media() {
		}

		public Media(String code, String name, Integer type, int weight,
				String template, String description) {
			this.code = code;
			this.name = name;
			this.type = type;
			this.weight = type * 1000 + weight;
			this.template = template;
			this.description = description;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getType() {
			return type;
		}

		public void setType(Integer type) {
			this.type = type;
		}

		public int getWeight() {
			return weight;
		}

		public void setWeight(int weight) {
			this.weight = weight;
		}

		public String getTemplate() {
			return template;
		}

		public void setTemplate(String template) {
			this.template = template;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}

	/**
	 * 校正影像资料信息
	 * @param mediaList
	 * @return
	 */
	public static List<BaoxianBaseInfoMediaDTO> fixMediaTemplate(
			List<BaoxianBaseInfoMedia> mediaList) {
		if (mediaList == null) {
			return null;
		}

		List<BaoxianBaseInfoMediaDTO> ret = Lists.newArrayList();
		for (BaoxianBaseInfoMedia media : mediaList) {
			BaoxianBaseInfoMediaDTO mediaDTO = new BaoxianBaseInfoMediaDTO();
			mediaDTO = CommonUtil.simpleValueCopy(media, mediaDTO);

			MediaDictionary.Media m = MediaDictionary.findByCode(mediaDTO
					.getCode());
			if (m != null) {
				mediaDTO.setTemplate(m.getTemplate());
				mediaDTO.setDescription(m.getDescription());
			}

			ret.add(mediaDTO);
		}

		return ret;
	}

	public static void main(String[] argv) {
		MediaDictionary.Media template = MediaDictionary
				.findByCode(MediaDictionary.DrivingLicenseCode);
		System.console().printf("{0}", template.getCode());
	}

}
