package com.chengniu.client.pojo;

import java.util.List;

public class BaoxianIntentDTO extends BaoxianUnderwritingDTO {

    private BaoxianIntent intent;
    private BaoxianBaseInfo baseInfo;
    private List<BaoxianInformalReport> informalReportList;

    public BaoxianIntent getIntent() {
        return intent;
    }

    public void setIntent(BaoxianIntent intent) {
        this.intent = intent;
    }

    public BaoxianBaseInfo getBaseInfo() {
        return baseInfo;
    }

    public void setBaseInfo(BaoxianBaseInfo baseInfo) {
        this.baseInfo = baseInfo;
    }

    public List<BaoxianInformalReport> getInformalReportList() {
        return informalReportList;
    }

    public void setInformalReportList(List<BaoxianInformalReport> informalReportList) {
        this.informalReportList = informalReportList;
    }
}