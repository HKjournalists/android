package com.chengniu.client.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.chengniu.client.pojo.BaoxianCityFanhua;

@Repository("baoxianCityDAO")
public class BaoxianCityDAO extends SuperDAO<BaoxianCityFanhua, String> {
	@Override
	protected String namespace() {
		return "mybatis.xml.BaoxianCityFanhuaMapper";
	}

	public int delete(BaoxianCityFanhua info) {
		return this.getSqlSession().update(this.tip("delete"), info);
	}

	public List<BaoxianCityFanhua> queryCity(BaoxianCityFanhua info) {
		return this.getSqlSession().selectList(this.tip("queryCity"), info);
	}

	public BaoxianCityFanhua queryByCityName(String cityName) {
		return this.getSqlSession().selectOne(this.tip("queryByCityName"),
                cityName);
	}

	public BaoxianCityFanhua queryCityInfo(String cityCode, Integer kind) {
		BaoxianCityFanhua info = new BaoxianCityFanhua();
		info.setCityCode(cityCode);
		info.setKind(kind);
		return this.getSqlSession().selectOne(this.tip("queryCityInfo"), info);
	}

	/**
	 * 获取所有的城市
	 *
	 * @return
	 */
	public List<BaoxianCityFanhua> findAllOrderByKindAsc() {
		return this.getSqlSession().selectList(
                this.tip("findAllOrderByKindAsc"));
	}

    public List<BaoxianCityFanhua> findCitiesOrderByCodeAsc() {
        return this.getSqlSession().selectList(
                this.tip("findCitiesOrderByCodeAsc"));
    }
}