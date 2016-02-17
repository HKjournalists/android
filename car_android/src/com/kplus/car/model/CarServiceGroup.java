package com.kplus.car.model;

import com.kplus.car.parser.ApiListField;

import java.util.List;

/**
 * Description：服务分组列表
 * <br/><br/>Created by FU ZHIXUE on 2015/8/14.
 * <br/><br/>
 */
public class CarServiceGroup extends BaseModelObj {

    @ApiListField("id")
    private Long id;
    /**
     * 分组名称
     */
    @ApiListField("name")
    private String name;
    /**
     * 是否系统分组
     */
    @ApiListField("system")
    private Boolean system;
    /**
     * 是否显示分组名称
     */
    @ApiListField("showName")
    private Boolean showName;
    /**
     * 首页展示数
     */
    @ApiListField("indexServiceCount")
    private Integer indexServiceCount;
    /**
     * 包含服务数
     */
    @ApiListField("serviceCount")
    private Integer serviceCount;
    /**
     * 排序号
     */
    @ApiListField("sort")
    private Integer sort;
    /**
     * 服务列表
     */
    @ApiListField("services")
    private List<CarService> services;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSystem() {
        return system;
    }

    public void setSystem(Boolean system) {
        this.system = system;
    }

    public Boolean getShowName() {
        return showName;
    }

    public void setShowName(Boolean showName) {
        this.showName = showName;
    }

    public Integer getIndexServiceCount() {
        return indexServiceCount;
    }

    public void setIndexServiceCount(Integer indexServiceCount) {
        this.indexServiceCount = indexServiceCount;
    }

    public Integer getServiceCount() {
        return serviceCount;
    }

    public void setServiceCount(Integer serviceCount) {
        this.serviceCount = serviceCount;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public List<CarService> getServices() {
        return services;
    }

    public void setServices(List<CarService> services) {
        this.services = services;
    }
}
