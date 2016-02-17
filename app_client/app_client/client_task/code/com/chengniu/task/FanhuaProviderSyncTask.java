package com.chengniu.task;

import com.chengniu.client.common.CommonUtil;
import com.chengniu.client.common.DateUtils;
import com.chengniu.client.common.HttpClientUtil;
import com.chengniu.client.common.SerializableUtils;
import com.chengniu.client.dao.BaoxianCityDAO;
import com.chengniu.client.dao.BaoxianCompanyDictDAO;
import com.chengniu.client.pojo.BaoxianCityFanhua;
import com.chengniu.client.pojo.BaoxianCompany;
import com.chengniu.client.pojo.BaoxianCompanyDict;
import com.chengniu.client.pojo.BaoxianCompanyFanhua;
import com.chengniu.client.service.BaoxianApi;
import com.chengniu.client.service.BaoxianCompanyService;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.Action;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class FanhuaProviderSyncTask {
    protected static final Logger log = LogManager
            .getLogger(FanhuaProviderSyncTask.class);

    @Autowired
    private BaoxianCityDAO baoxianCityDAO;

    @Autowired
    private BaoxianCompanyDictDAO baoxianCompanyDictDAO;

    @Autowired
    private BaoxianCompanyService baoxianCompanyService;

    @Autowired
    private BaoxianApi fanhuaApi;

    @Value("${baoxian.api.url}")
    private String bxApiUrl;

    private Map<String, Object> resultMap;

    public Map<String, Object> getResultMap() {
        return resultMap;
    }

    private static Boolean running = false;
    private static Date lastSyncTime;

    public String sync() {
        // 5分钟内不让同步
        if (lastSyncTime != null) {
            Date now = new Date();

            if (now.getTime() - lastSyncTime.getTime() < 5 * 60 * 1000) {
                resultMap.put("result", false);
                resultMap.put("lastSyncTime", DateUtils.format(lastSyncTime, DateUtils.DATE_FORMAT_YYYYMMDDHHMMSS));
                resultMap.put("msg", "操作太频繁");
                return Action.SUCCESS;
            }
        }

        resultMap = Maps.newHashMap();
        synchronized (running) {
            if (running) {
                resultMap.put("result", false);
                if (lastSyncTime != null) {
                    resultMap.put("lastSyncTime",
                            DateUtils.format(lastSyncTime, DateUtils.DATE_FORMAT_YYYYMMDDHHMMSS));
                } else {
                    resultMap.put("lastSyncTime", "");
                }
                resultMap.put("msg", "正在同步中");
                return Action.SUCCESS;
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                execute();
            }
        }).start();

        resultMap.put("result", true);
        if (lastSyncTime != null) {
            resultMap.put("lastSyncTime",
                    DateUtils.format(lastSyncTime, DateUtils.DATE_FORMAT_YYYYMMDDHHMMSS));
        } else {
            resultMap.put("lastSyncTime", "");
        }
        resultMap.put("msg", "已接受，同步需要花费较长时间，请耐心等待同步结束");
        return Action.SUCCESS;
    }

    public void execute() {
        synchronized (running) {
            running = true;
        }

        log.info("开始");
        try {
            syncProvidersWithFanhua();
            lastSyncTime = new Date();
        } catch (Exception e) {
            log.info("同步失败");
        }
        log.info("结束");

        synchronized (running) {
            running = false;
        }
    }

    private void syncProvidersWithFanhua() {
        // 获取所有城市
        List<BaoxianCityFanhua> baoxianCityFanhuaList = baoxianCityDAO.findCitiesOrderByCodeAsc();

        for (BaoxianCityFanhua city: baoxianCityFanhuaList) {
            try {
                // 获取泛华的服务商配置数据
                List<BaoxianCompanyFanhua> companyFanhuaList = fanhuaApi.queryCompaniesInCity(city.getCityCode());

                // 获取已有的保险公司
                List<BaoxianCompany> existCompanies = baoxianCompanyService.queryCompaniesInCityByChannel(city.getCityCode(), "fanhua");

                final String provinceCode = city.getCityCode().substring(0, 2) + "0000";
                boolean changed = false;

                // 对每条配置数据逐一处理
                for (BaoxianCompanyFanhua companyFanhua : companyFanhuaList) {
                    // 获取存在的保险公司
                    BaoxianCompany company = searchCompanies(existCompanies, companyFanhua.getCode());
                    if (company == null) {
                        // 新增保险公司
                        String companyShortName = fixCompanyName(companyFanhua.getName());
                        BaoxianCompanyDict companyDict = baoxianCompanyDictDAO.selectCompanyShortName(companyShortName);
                        if (companyDict == null) {
                            log.debug("找不到保险公司:{}, 无法新增机构:{}",
                                    companyFanhua.getName(),
                                    CommonUtil.gson().toJson(companyFanhua));
                        } else {
                            try {
                                company = new BaoxianCompany();
                                company.setChannel("fanhua");
                                company.setCityCode(companyFanhua.getCityCode());
                                company.setProvince(provinceCode);
                                company.setCode(companyFanhua.getCode());
                                company.setName(companyDict.getShortname());
                                company.setBaiduName(companyDict.getShortname());
                                company.setCompanyName(companyDict.getFullname());
                                company.setPic(companyDict.getLogo());
                                company.setPicSmall(companyDict.getLogoSmall());
                                company.setRemark(companyDict.getRemark());
                                company.setOpenInfo(2);
                                company.setSupportAutomatic(true);
                                company.setCreateTime(new Date());
                                company.setId(SerializableUtils.allocUUID());
                                company.setSyncFlag(true);
                                company.setChannelStatus(true);

                                baoxianCompanyService.insert(company);
                                changed = true;
                            } catch (Exception e) {
                                log.debug("添加保险机构失败: {} cause: {}", CommonUtil.gson().toJson(company), e.getCause());
                            }
                        }
                    } else {
                        if (null != existCompanies) {
                            existCompanies.remove(company);
                        }

                        // 更新状态
                        if (company.getChannelStatus() == null || !company.getChannelStatus()) {
                            company.setChannelStatus(true);
                            company.setSyncFlag(true);
                            company.setUpdateTime(new Date());
                            baoxianCompanyService.updateCompanyEnabled(company);
                            changed = true;
                        }
                    }
                }

                // 关闭其它保险机构
                if (existCompanies != null) {
                    for (BaoxianCompany company : existCompanies) {
                        if (company.getChannelStatus() == null || company.getChannelStatus()) {
                            company.setChannelStatus(false);
                            company.setSyncFlag(true);
                            company.setUpdateTime(new Date());
                            baoxianCompanyService.updateCompanyEnabled(company);
                            changed = true;
                        }
                    }
                }

                // 更新接口层缓存
                if (changed) {
                    notifyCompanyChanged(provinceCode, city.getCityCode());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String fixCompanyName(String name) {
        String companyShortName = name;
        if (companyShortName.length() > 4) {
            companyShortName = companyShortName.substring(0, 4);
        } else if (companyShortName.length() > 2) {
            companyShortName = companyShortName.substring(0, 2);
        }

        if ("天平".equals(companyShortName)) {
            companyShortName = "安盛";
        } else if ("永城".equals(companyShortName)) {
            companyShortName = "永诚";
        } else if ("dd".equals(companyShortName)) {
            companyShortName = "大地";
        }
        return companyShortName;
    }

    private void notifyCompanyChanged(String provinceCode, String cityCode) throws Exception {
        String url = bxApiUrl + "/app/insurance/city/modify/" + provinceCode + "/" + cityCode;
        HttpClientUtil.doGet(url);
    }

    private BaoxianCompany searchCompanies(List<BaoxianCompany> existCompanies, String code) {
        if (existCompanies == null || existCompanies.isEmpty()) {
            return null;
        }

        for (BaoxianCompany company : existCompanies) {
            if (code.equals(company.getCode())) {
                return company;
            }
        }
        return null;
    }
}
