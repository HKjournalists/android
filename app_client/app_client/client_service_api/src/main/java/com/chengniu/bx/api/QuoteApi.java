package com.chengniu.bx.api;

import com.chengniu.bx.api.dto.BaoxianInformalReportDTO;
import com.chengniu.bx.api.exception.DisposeException;

/**
 * 报价API
 */
public interface QuoteApi {

    /**
     * 更新报价
     *
     * @param reportDTO
     * @return
     */
    String updateInformalReport(BaoxianInformalReportDTO reportDTO) throws DisposeException;

    /**
     * 标记报价失败
     * @param reportId
     * @param remark
     * @return
     * @throws DisposeException
     */
    boolean reportInformalReportFailed(String reportId, String remark) throws DisposeException;

}
