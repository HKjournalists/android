package com.chengniu.client.service.impl;

import com.chengniu.client.common.CommonUtil;
import com.chengniu.client.common.MediaDictionary;
import com.chengniu.client.common.SerializableUtils;
import com.chengniu.client.dao.*;
import com.chengniu.client.domain.MediaStatus;
import com.chengniu.client.pojo.*;
import com.chengniu.client.service.BaoxianBaseInfoService;
import com.kplus.orders.rpc.common.DateUtils;
import com.kplus.orders.rpc.dto.PageVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("baoxianBaseInfoService")
public class BaoxianBaseInfoServiceImpl implements BaoxianBaseInfoService {
	@Resource(name = "baoxianUnderwritingDateExceptionDAO")
	private BaoxianUnderwritingDateExceptionDAO baoxianUnderwritingDateExceptionDAO;
	@Resource(name = "baoxianBaseInfoDAO")
	private BaoxianBaseInfoDAO baoxianBaseInfoDAO;
	@Resource(name = "baoxianCityMappingDAO")
	private BaoxianCityMappingDAO baoxianCityMappingDAO;
	@Resource(name = "baoxianCityDAO")
	private BaoxianCityDAO baoxianCityDAO;
	@Resource(name = "baoxianBaseInfoMediaDAO")
	private BaoxianBaseInfoMediaDAO baoxianBaseInfoMediaDAO;

	@Override
	public List<BaoxianBaseInfo> queryByUserId(String userId, Integer userType) {
		return this.baoxianBaseInfoDAO.queryByUserId(userId, userType);
	}

	@Override
	public BaoxianBaseInfo query(String id, String userId, Integer userType) {
		BaoxianBaseInfo info = this.baoxianBaseInfoDAO.query(id);
		if (info != null && StringUtils.hasText(info.getUserId())
				&& info.getUserId().equals(userId) && userType != null
				&& userType.compareTo(info.getUserType()) == 0) {
			return info;
		} else {
			return null;
		}
	}

	@Override
	public BaoxianBaseInfo queryByVehicleNum(Integer userType, String userId, String vehicleNum) {
		return baoxianBaseInfoDAO.queryByVehicleNum(userType, userId, vehicleNum);
	}

	@Override
	public boolean save(BaoxianBaseInfo info) {
		if (info == null)
			return false;
		if (info.getToubaoren() != null && info.getToubaoren()) {
			info.setToubaorenCardName(info.getIdCardName());
			info.setToubaorenCardNum(info.getIdCardNum());
		}
		if (info.getBeibaoren() != null && info.getBeibaoren()) {
			info.setBeibaorenCardName(info.getIdCardName());
			info.setBeibaorenCardNum(info.getIdCardNum());
		}
		if (info.getIdCardType() == null) {
			info.setIdCardType(1);
		}
		if (info.getBeibaorenCardType() == null)
			info.setBeibaorenCardType(info.getIdCardType());
		if (info.getToubaorenCardType() == null)
			info.setToubaorenCardType(info.getIdCardType());
		info.setUpdateTime(new Date());
		info.setDrivingChecked(0);
		info.setIdCardChecked(0);
		if (!StringUtils.hasText(info.getId())) {
			if (!StringUtils.hasText(info.getVehicleModelPrice()))
				info.setVehicleModelPrice("0");
			info.setId(SerializableUtils.allocUUID());
			info.setCreateTime(info.getUpdateTime());
			dealProvider(info);
			info.setDeleted(false);
			boolean succ = this.baoxianBaseInfoDAO.insert(info) > 0;
			if (succ) {
				migrateMediaInfo(info, null);
			}
			return succ;
		} else {
			BaoxianBaseInfo bs = this.baoxianBaseInfoDAO.query(info.getId());
			if (bs == null) {
				info.setId(SerializableUtils.allocUUID());
				dealProvider(info);
				info.setCreateTime(info.getUpdateTime());
				boolean succ = this.baoxianBaseInfoDAO.insert(info) > 0;
				if (succ) {
					migrateMediaInfo(info, null);
				}
				return succ;
			}

			boolean isDrivingLicenseChanged = bs.getDrivingUrl() != null
					&& bs.getDrivingUrl().equals(info.getDrivingUrl());
			if (!isDrivingLicenseChanged) {
				info.setDrivingChecked(bs.getDrivingChecked());
			}

			boolean isIDCardChanged = (info.getIdCardChecked() != null && info
					.getIdCardChecked().compareTo(bs.getIdCardChecked()) != 0)
					|| (info.getIdCardType() != null && !info.getIdCardType()
							.equals(bs.getIdCardChecked()))
					|| (info.getIdCardUrl() != null && !info.getIdCardUrl()
							.equals(bs.getIdCardUrl()));
			if (!isIDCardChanged) {
				info.setIdCardChecked(bs.getIdCardChecked());
			}
			dealProvider(info);
			migrateMediaInfo(info, bs);
			return this.baoxianBaseInfoDAO.update(info) > 0;
		}
	}

	/**
	 * 处理商户端不需要认证的部分
	 *
	 * @param info
	 */
	private void dealProvider(BaoxianBaseInfo info) {
		// 商户早先非上传图片版本不需要认证
		if (info.getUserType() == BaoxianBaseInfo.USER_TYPE.PROVIDER.value
				&& StringUtils.isEmpty(info.getIdCardUrl())
				&& !StringUtils.isEmpty(info.getIdCardNum())) {
			info.setDrivingChecked(BaoxianBaseInfo.DRIVING_CHECKED.SUCCESS.value);
			info.setIdCardChecked(BaoxianBaseInfo.ID_CARD_CHECKED.SUCCESS.value);
		}
	}

	@Override
	public List<BaoxianCityFanhua> queryCity(BaoxianCityFanhua info) {
		return this.baoxianCityDAO.queryCity(info);
	}

	@Override
	public BaoxianCityFanhua queryCityInfo(String cityCode, Integer kind) {
		return this.baoxianCityDAO.queryCityInfo(cityCode, kind);
	}

	@Override
	public BaoxianCityFanhua queryByCityName(String cityName) {
		return this.baoxianCityDAO.queryByCityName(cityName);
	}

	@Override
	public boolean delete(BaoxianBaseInfo info) {
		info.setUpdateTime(new Date());
		return this.baoxianBaseInfoDAO.deleteInfo(info) > 0;
	}

	@Override
	public PageVO<BaoxianBaseInfo> page(SearchVO search) {
		return this.baoxianBaseInfoDAO.queryByPage(search);
	}

	@Override
	public BaoxianCityMapping queryCity(String code) {
		try {
			return baoxianCityMappingDAO.queryCity(code);
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public BaoxianBaseInfo queryLastInfo(String userId, Integer userType) {
		BaoxianBaseInfo list = this.baoxianBaseInfoDAO.queryLastInfo(userId,
				userType);
		return list;
	}

	@Override
	public String queryLastWork(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(new Date());
		if (c1.get(Calendar.DATE) > c.get(Calendar.DATE)) {
			date = new Date();
		}
		BaoxianUnderwritingDateException dateinfo = this.baoxianUnderwritingDateExceptionDAO
				.queryWorkDayByDate(date);
		StringBuilder sb = new StringBuilder();
		sb.append(DateUtils.format(dateinfo.getDate(), "yyyy-MM-dd "));
		if (!DateUtils.format(date, "yyyy-MM-dd ").equals(sb.toString())) {
			c.setTime(dateinfo.getDate());
		}
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		if ((hour >= 17 && minute >= 30) || hour >= 18 || hour < 9) {
			if (DateUtils.format(date, "yyyy-MM-dd ").equals(sb.toString())) {
				c.add(Calendar.DATE, 1);
				dateinfo = this.baoxianUnderwritingDateExceptionDAO
						.queryWorkDayByDate(c.getTime());
			}
			c.setTime(dateinfo.getDate());
			sb = new StringBuilder();
			sb.append(DateUtils.format(dateinfo.getDate(), "yyyy-MM-dd "));
			sb.append("10:00");
		} else if ((hour >= 11 && minute > 30) && (hour < 14 && minute <= 30)) {
			sb.append("14:00");
		} else {
			c.add(Calendar.MINUTE, 30);
			return DateUtils.format(c.getTime(), "yyyy-MM-dd HH:mm:ss");
		}
		return sb.toString();
	}

	@Override
	public List<BaoxianBaseInfoMedia> queryMediaInfo(String underwritingId) {
		List<BaoxianBaseInfoMedia> mediaList = this.baoxianBaseInfoMediaDAO
				.selectListById(underwritingId);
		return mediaList;
	}

	@Override
	public void updateMedia(BaoxianBaseInfoMedia media) throws Exception {
		BaoxianBaseInfoMedia old = this.baoxianBaseInfoMediaDAO
				.selectByPrimaryKey(media);
		if (old == null) {
//			!MediaStatus.isUserFixable(old.getStatus()
			// throw new DisposeException("该影像资料不需要补全或已锁定");
			MediaDictionary.Media template = MediaDictionary.findByCode(media.getCode());
			media.setName(template.getName());
			media.setStatus(MediaStatus.VERIFY_PENDING.ordinal());
			baoxianBaseInfoMediaDAO.insert(media);
			return;
		}

		media.setUpdateTime(new Date());
		media.setStatus(MediaStatus.VERIFY_PENDING.ordinal());
		this.baoxianBaseInfoMediaDAO.updateByPrimaryKeySelective(media);

		if (MediaDictionary.IDCardCode.equalsIgnoreCase(media.getCode())) {
			BaoxianBaseInfo info = baoxianBaseInfoDAO.query(media.getId());
			// 如果证件类型不同
			if (1 != info.getIdCardType()
					|| media.getUrl() != info.getIdCardUrl()
					&& !media.getUrl().equals(info.getIdCardUrl())) {

				BaoxianBaseInfo newInfo = new BaoxianBaseInfo();
				newInfo.setId(info.getId());
				newInfo.setIdCardChecked(0);
				newInfo.setIdCardType(1);
				newInfo.setIdCardUrl(media.getUrl());

				baoxianBaseInfoDAO.updateUserIDUrl(newInfo);
			}

		} else if (MediaDictionary.AgencyCode.equalsIgnoreCase(media.getCode())) {
			BaoxianBaseInfo info = baoxianBaseInfoDAO.query(media.getId());
			// 如果证件类型不同
			if (21 != info.getIdCardType()
					|| media.getUrl() != info.getIdCardUrl()
					&& !media.getUrl().equals(info.getIdCardUrl())) {

				BaoxianBaseInfo newInfo = new BaoxianBaseInfo();
				newInfo.setId(info.getId());
				newInfo.setIdCardChecked(0);
				newInfo.setIdCardType(21);
				newInfo.setIdCardUrl(media.getUrl());

				baoxianBaseInfoDAO.updateUserIDUrl(newInfo);
			}
		} else if (MediaDictionary.DrivingLicenseCode.equalsIgnoreCase(media
				.getCode())) {
			BaoxianBaseInfo info = baoxianBaseInfoDAO.query(media.getId());
			// 如果证件类型不同
			if (media.getUrl() != info.getDrivingUrl()
					&& !media.getUrl().equals(info.getDrivingUrl())) {

				BaoxianBaseInfo newInfo = new BaoxianBaseInfo();
				newInfo.setId(info.getId());
				newInfo.setDrivingId(null);
				newInfo.setDrivingChecked(0);
				newInfo.setDrivingUrl(media.getUrl());
				newInfo.setVehicleNum(null);
				newInfo.setFrameNum(null);
				newInfo.setMotorNum(null);
				newInfo.setRegisterDate(null);

				baoxianBaseInfoDAO.updateDrivingLicenseUrl(newInfo);
			}
		}
	}

	/**
	 * 迁移认证的证件到影像资料
	 * 
	 * @param info
	 * @throws Exception
	 */
	private void migrateMediaInfo(BaoxianBaseInfo info, BaoxianBaseInfo bs) {
		// 身份证
		if (info.getIdCardUrl() != null) {
			if (bs != null) {
				// 如果证件变了，删除旧的
				if ((bs.getIdCardChecked() != null && bs.getIdCardChecked()
						.compareTo(info.getIdCardChecked()) == 0)
						|| !info.getIdCardType().equals(bs.getIdCardType())
						|| (info.getIdCardUrl() != null && !info.getIdCardUrl()
								.equals(bs.getIdCardUrl()))) {
					BaoxianBaseInfoMediaKey key = new BaoxianBaseInfoMediaKey();
					key.setId(info.getId());
					key.setCode(MediaDictionary.IDCardCode);
					baoxianBaseInfoMediaDAO.deleteByPrimaryKey(key);

					key.setCode(MediaDictionary.AgencyCode);
					baoxianBaseInfoMediaDAO.deleteByPrimaryKey(key);
				}
			}

			String code = MediaDictionary.IDCardCode;
			if (info.getIdCardType() != null && info.getIdCardType() == 21) {
				code = MediaDictionary.AgencyCode;
			}
			MediaDictionary.Media template = MediaDictionary.findByCode(code);

			BaoxianBaseInfoMedia media = new BaoxianBaseInfoMedia();
			media.setId(info.getId());
			media.setCode(template.getCode());
			media.setUrl(info.getIdCardUrl());
			media.setWeight(template.getWeight());
			media.setName(template.getName());
			media.setUpdateTime(new Date());
			media.setStatus(MediaStatus.VERIFY_PENDING.ordinal());
			if (info.getIdCardChecked() != null && info.getIdCardChecked() == 1) {
				media.setStatus(MediaStatus.VERIFIED.ordinal());
			}

			try {
				saveMedia(media);
			} catch (Exception e) {
			}
		}

		// 驾驶证
		if (info.getDrivingUrl() != null) {
			if (bs != null) {
				// 如果证件变了，删除旧的
				if ((info.getDrivingUrl() != null && !info.getDrivingUrl()
						.equals(bs.getDrivingUrl()))) {
					BaoxianBaseInfoMediaKey key = new BaoxianBaseInfoMediaKey();
					key.setId(info.getId());
					key.setCode(MediaDictionary.DrivingLicenseCode);
					baoxianBaseInfoMediaDAO.deleteByPrimaryKey(key);
				}
			}

			MediaDictionary.Media template = MediaDictionary
					.findByCode(MediaDictionary.DrivingLicenseCode);
			BaoxianBaseInfoMedia media = new BaoxianBaseInfoMedia();
			media.setId(info.getId());
			media.setCode(template.getCode());
			media.setUrl(info.getDrivingUrl());
			media.setName(template.getName());
			media.setWeight(template.getWeight());
			media.setUpdateTime(new Date());
			media.setStatus(MediaStatus.VERIFY_PENDING.ordinal());
			if (info.getDrivingChecked() != null
					&& info.getDrivingChecked() == 1) {
				media.setStatus(MediaStatus.VERIFIED.ordinal());
			}

			try {
				saveMedia(media);
			} catch (Exception e) {
			}
		}
	}

	private void saveMedia(BaoxianBaseInfoMedia media) {
		media.setCreateTime(new Date());
		baoxianBaseInfoMediaDAO.insert(media);
	}
}