package com.chengniu.client.dao;

import com.chengniu.client.pojo.BaoxianQuoteInfo;
import com.chengniu.client.pojo.BaoxianQuoteInfoWithBLOBs;
import org.springframework.stereotype.Repository;

@Repository("baoxianQuoteInfoDAO")
public class BaoxianQuoteInfoDAO extends
		SuperDAO<BaoxianQuoteInfoWithBLOBs, String> {

	@Override
	protected String namespace() {
		return "mybatis.xml.BaoxianQuoteInfoMapper";
	}

	public BaoxianQuoteInfoWithBLOBs queryByQuoteId(String quoteId) {
		return this.getSqlSession().selectOne(this.tip("queryByQuoteId"),
				quoteId);
	}

    public BaoxianQuoteInfoWithBLOBs queryByUnderwritingIdAndQuoteId(String underwritingId, String quoteId) {
        BaoxianQuoteInfoWithBLOBs info = new BaoxianQuoteInfoWithBLOBs();
        info.setBaoxianUnderwritingId(underwritingId);
        info.setQuoteId(quoteId);
        return this.getSqlSession().selectOne(this.tip("queryByUnderwritingIdAndQuoteId"),
                info);
    }

	public BaoxianQuoteInfoWithBLOBs queryLastByQuoteIdAndStep(String quoteId,
			Integer step) {
		BaoxianQuoteInfoWithBLOBs info = new BaoxianQuoteInfoWithBLOBs();
		info.setQuoteId(quoteId);
		info.setStep(step);
		return this.getSqlSession().selectOne(
				this.tip("queryLastByQuoteIdAndStep"), info);
	}

	public BaoxianQuoteInfoWithBLOBs queryLastRequestInfo(
			String baoxianUnderwritingId, Integer step) {
		BaoxianQuoteInfoWithBLOBs info = new BaoxianQuoteInfoWithBLOBs();
		info.setBaoxianUnderwritingId(baoxianUnderwritingId);
		info.setStep(step);
		return this.getSqlSession().selectOne(this.tip("queryLastRequestInfo"),
                info);
	}

	public int updateReponse(BaoxianQuoteInfoWithBLOBs info) {
		return this.getSqlSession().update(this.tip("updateReponse"), info);
	}

	public void updateStep(BaoxianQuoteInfo info) {
		this.getSqlSession().update(this.tip("updateStep"), info);
	}

	public void updateStatus(BaoxianQuoteInfo info) {
		this.getSqlSession().update(this.tip("updateStatus"), info);
	}

	public void updateUnderWritingId(String old, String newid) {
		BaoxianQuoteInfoWithBLOBs bols = new BaoxianQuoteInfoWithBLOBs();
		bols.setQuoteId(newid);
		bols.setBaoxianUnderwritingId(old);
		this.getSqlSession().update(this.tip("updateUnderWritingId"), bols);
	}

}