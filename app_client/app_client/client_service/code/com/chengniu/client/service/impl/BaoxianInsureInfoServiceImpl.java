package com.chengniu.client.service.impl;

import com.chengniu.client.common.SerializableUtils;
import com.chengniu.client.dao.BaoxianInsureInfoDAO;
import com.chengniu.client.dao.BaoxianPeisongDAO;
import com.chengniu.client.dao.BaoxianUnderwritingExpressDAO;
import com.chengniu.client.event.UnderwritingExpressEvent;
import com.chengniu.client.event.dispatcher.EventDispatcher;
import com.chengniu.client.pojo.*;
import com.chengniu.client.service.*;
import com.kplus.orders.rpc.dto.PageVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service("baoxianInsureInfoService")
public class BaoxianInsureInfoServiceImpl implements BaoxianInsureInfoService {
    protected static final Logger log = LogManager
            .getLogger(BaoxianInsureInfoServiceImpl.class);

    @Autowired
	private BaoxianInsureInfoDAO baoxianInsureInfoDAO;
    @Autowired
	private BaoxianPeisongDAO baoxianPeisongDAO;
	@Autowired
	private BaoxianOrderService baoxianOrderService;
    @Autowired
	public BaoxianBaseInfoService baoxianBaseInfoService;
    @Autowired
	public BaoxianUnderwritingExpressDAO baoxianUnderwritingExpressDAO;
    @Autowired
	private BaoxianUnderwritingService baoxianUnderwritingService;

    @Autowired
    private FanhuaService fanhuaService;

	@Autowired
	private EventDispatcher eventDispatcher;

    // 延迟执行队列
    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(4,
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("DeliveryAddressUpdater");
                    thread.setDaemon(true);
                    thread.setPriority(Thread.NORM_PRIORITY);

                    return thread;
                }
            },
            new ThreadPoolExecutor.CallerRunsPolicy());

	@Override
	public boolean disposePeisong(BaoxianUnderwriting underwriting, BaoxianPeisong info) {
		if (info == null)
			return false;
		if (!StringUtils.hasText(info.getId())) {
			info.setId(SerializableUtils.allocUUID());
			info.setCreateTime(new Date());
			info.setDeleted(false);
			info.setUpdateTime(new Date());
			this.baoxianPeisongDAO.insert(info);
		} else {
			info.setUpdateTime(new Date());
			info.setDeleted(false);
			this.baoxianPeisongDAO.update(info);
		}

		BaoxianInsureInfo bis = this.queryByUnderwritingId(underwriting.getId());
		if (bis == null) {
			bis = new BaoxianInsureInfo();
			bis.setCreateTime(new Date());
			bis.setUserId(info.getUserId());
			bis.setUserType(info.getUserType());
			bis.setCityCode(info.getCityCode());
			bis.setCityName(info.getCityName());
			bis.setVehicleModelCode(underwriting.getVehicleModelCode());
			bis.setVehicleModelName(underwriting.getVehicleModelName());
			bis.setCityName(underwriting.getCityName());
			BaoxianBaseInfo baseInfo = this.baoxianBaseInfoService
					.queryLastInfo(underwriting.getUserId(),
							underwriting.getUserType());
			bis.setDrivingId(baseInfo.getDrivingId());
			bis.setIdCardName(baseInfo.getIdCardName());
			bis.setGuohu(baseInfo.getGuohu());
			bis.setIdCardNum(baseInfo.getIdCardNum());
		}
		if (info.getAddress() == null)
			info.setAddress("");
		bis.setPeisongAddress(info.getProvinceName() + info.getCityName()
				+ (info.getTownName() != null ? info.getTownName() : "")
				+ info.getAddress());
		bis.setBaoxianPeisongId(info.getId());
		bis.setPeisongProvince(info.getProvinceName());
		bis.setIdCardAddress(info.getIdCardAddress());
		bis.setPeisongCity(info.getCityName());
		bis.setPeisongTown(info.getTownName());
		bis.setBaoxianUnderwritingId(underwriting.getId());
		bis.setPeisongName(info.getName());
		bis.setPeisongMobile(info.getMobile());
		bis = this.save(bis);

		boolean succ = this.baoxianUnderwritingService.updateInsuerInfo(underwriting.getId(), bis.getId());
        if (succ) {
            // 在报价阶段修改配送地址
            try {
                fanhuaService.updatePeisongInQuoteStage(underwriting, bis);
            } catch (Exception e) {
                log.debug("修改报价阶段配送信息失败：{}", underwriting.getId());
            }
        }

        return true;
	}

    @Override
    public void disposePeisong(BaoxianUnderwriting underwriting) {
        if ("fanhua".equalsIgnoreCase(underwriting.getChannel())) {
            final String id = underwriting.getId();
            executor.schedule(new Runnable() {
                @Override
                public void run() {
                    // 提交配送信息
                    try {
                        Boolean ret = fanhuaService.updatePeisong(id);
                        log.debug("提交配送信息：{} 结果：{}", id, ret);
                    } catch (Exception e) {
                        log.debug("提交配送信息失败:{}, {}", id, e);
                    }
                }
            }, 60, TimeUnit.SECONDS);
        }
    }

    @Override
    public BaoxianInsureInfo save(BaoxianInsureInfo info) {
        if (StringUtils.hasText(info.getId()))
            this.baoxianInsureInfoDAO.update(info);
        else {
            info.setId(SerializableUtils.allocUUID());
            this.baoxianInsureInfoDAO.insert(info);
        }
        return info;
    }

    @Override
    public boolean deletePeisong(BaoxianPeisong peisong) {
        return this.baoxianPeisongDAO.delete(peisong) > 0;
    }

    @Override
	public BaoxianInsureInfo query(String id) {
		return this.baoxianInsureInfoDAO.query(id);
	}

	@Override
	public PageVO<BaoxianInsureInfo> page(SearchVO vo) {
		if (vo == null || vo.getUserId() == null || vo.getUserType() == null)
			return null;
		return this.baoxianInsureInfoDAO.queryByPage(vo);
	}

	@Override
	public PageVO<BaoxianPeisong> pagePeisong(SearchVO vo) {
		if (vo == null || vo.getUserId() == null || vo.getUserType() == null)
			return null;
		return this.baoxianPeisongDAO.queryByPage(vo);
	}

	@Override
	public BaoxianPeisong queryPeisong(String id, String userId,
			Integer userType) {
		BaoxianPeisong peisong = this.baoxianPeisongDAO.query(id);
		if (peisong != null) {
			if (userId.equals(peisong.getUserId())
					&& userType.compareTo(peisong.getUserType()) == 0)
				return peisong;
		}
		return null;
	}

	@Override
	public BaoxianUnderwritingExpress disposeExpress(
			BaoxianUnderwritingReport report, BaoxianUnderwritingExpress t) {
		t.setId(SerializableUtils.allocUUID());
		t.setCreateTime(new Date());
		t.setOperatorName("泛华保险");
		try {
			this.baoxianUnderwritingExpressDAO.insert(t);
			this.baoxianOrderService.updateExpressStatus(
					t.getBaoxianUnderwritingReportId(), t);

			// 发送事件
            log.debug("发布已配送事件失败 reportId:{}", report.getId());
            eventDispatcher.publish(UnderwritingExpressEvent.create(report, t));

		} catch (DuplicateKeyException e) {
		}
		return t;
	}

	@Override
	public BaoxianInsureInfo queryByUserId(String id, String userId,
			Integer userType) {
		BaoxianInsureInfo o = query(id);
		if (o != null) {
			if (userId.equals(o.getUserId())
					&& userType.compareTo(o.getUserType()) == 0)
				return o;
		}
		return null;
	}

	@Override
	public BaoxianInsureInfo queryByUnderwritingId(String id) {
		BaoxianInsureInfo info = this.baoxianInsureInfoDAO
				.queryByUnderwritingId(id);
		if (info != null) {
			if (info.getPeisongTown() == null)
				info.setPeisongTown("");
		}
		return info;
	}

	@Override
	public BaoxianUnderwritingExpress queryByBaoxianUnderwritingReportId(
			String id) {
		return this.baoxianUnderwritingExpressDAO
				.queryByBaoxianUnderwritingReportId(id);
	}
}