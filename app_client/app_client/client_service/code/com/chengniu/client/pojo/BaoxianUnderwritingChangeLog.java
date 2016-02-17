package com.chengniu.client.pojo;

import java.util.Date;

public class BaoxianUnderwritingChangeLog {

    public final static int OPERATION_UNCHANGE = 0;
    public final static int OPERATION_UPDATE = 1;
    public final static int OPERATION_ADD = 2;
    public final static int OPERATION_DELETED = 3;

    private Long id;

    private String baoxianUnderwritingId;

    private String fieldName;

    private String oldValue;

    private String newValue;

    private String operatorId;

    private String operatorName;

    private String remark;

    private Integer operation;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBaoxianUnderwritingId() {
        return baoxianUnderwritingId;
    }

    public void setBaoxianUnderwritingId(String baoxianUnderwritingId) {
        this.baoxianUnderwritingId = baoxianUnderwritingId == null ? null : baoxianUnderwritingId.trim();
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName == null ? null : fieldName.trim();
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue == null ? null : oldValue.trim();
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue == null ? null : newValue.trim();
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId == null ? null : operatorId.trim();
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName == null ? null : operatorName.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operationNo) {
        this.operation = operationNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}