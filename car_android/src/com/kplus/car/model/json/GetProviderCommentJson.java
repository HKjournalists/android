package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.ProviderComment;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

import java.util.List;

/**
 * Created by Administrator on 2015/8/24.
 */
public class GetProviderCommentJson extends BaseModelObj {
    @ApiListField("list")
    private List<ProviderComment> list;
    @ApiField("total")
    private Integer total;

    public List<ProviderComment> getList() {
        return list;
    }

    public void setList(List<ProviderComment> list) {
        this.list = list;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
