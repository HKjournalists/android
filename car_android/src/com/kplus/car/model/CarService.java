package com.kplus.car.model;

import com.kplus.car.parser.ApiListField;

/**
 * Description：服务列表
 * <br/><br/>Created by FU ZHIXUE on 2015/8/13.
 * <br/><br/>
 */
public class CarService extends BaseModelObj {

    @ApiListField("id")
    private Long id;
    /**
     * 服务名称
     */
    @ApiListField("name")
    private String name;
    /**
     * 服务短标题
     */
    @ApiListField("title")
    private String title;
    /**
     * 服务描述
     */
    @ApiListField("info")
    private String info;
    /**
     * 推荐标记
     */
    @ApiListField("flag")
    private Integer flag;
    /**
     * 优惠标签
     */
    @ApiListField("favorableTag")
    private String favorableTag;
    /**
     * 列表图标
     */
    @ApiListField("listIcon")
    private String listIcon;
    /**
     * 快捷入口图标
     */
    @ApiListField("linkIcon")
    private String linkIcon;
    /**
     * 主菜单图标
     */
    @ApiListField("tabIcon")
    private String tabIcon;
    /**
     * 点击行为类型
     */
    @ApiListField("motionType")
    private String motionType;
    /**
     * 点击行为内容
     */
    @ApiListField("motionValue")
    private String motionValue;

    @ApiListField("motionValueRelation")
    private String motionValueRelation;

    @ApiListField("transitionUrl")
    private String transitionUrl;
    /**
     * 排序号
     */
    @ApiListField("sort")
    private Integer sort;

    public CarService() {

    }

    public CarService copy() {
        CarService service = new CarService();
        service.setId(this.getId());
        service.setName(this.getName());
        service.setTitle(this.getTitle());
        service.setInfo(this.getInfo());
        service.setFlag(this.getFlag());
        service.setFavorableTag(this.getFavorableTag());
        service.setListIcon(this.getListIcon());
        service.setLinkIcon(this.getLinkIcon());
        service.setTabIcon(this.getTabIcon());
        service.setMotionType(this.getMotionType());
        service.setMotionValue(this.getMotionValue());
        service.setMotionValueRelation(this.getMotionValueRelation());
        service.setTransitionUrl(this.getTransitionUrl());
        service.setSort(this.getSort());
        return service;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getFavorableTag() {
        return favorableTag;
    }

    public void setFavorableTag(String favorableTag) {
        this.favorableTag = favorableTag;
    }

    public String getListIcon() {
        return listIcon;
    }

    public void setListIcon(String listIcon) {
        this.listIcon = listIcon;
    }

    public String getLinkIcon() {
        return linkIcon;
    }

    public void setLinkIcon(String linkIcon) {
        this.linkIcon = linkIcon;
    }

    public String getTabIcon() {
        return tabIcon;
    }

    public void setTabIcon(String tabIcon) {
        this.tabIcon = tabIcon;
    }

    public String getMotionType() {
        return motionType;
    }

    public void setMotionType(String motionType) {
        this.motionType = motionType;
    }

    public String getMotionValue() {
        return motionValue;
    }

    public void setMotionValue(String motionValue) {
        this.motionValue = motionValue;
    }

    public String getMotionValueRelation() {
        return motionValueRelation;
    }

    public void setMotionValueRelation(String motionValueRelation) {
        this.motionValueRelation = motionValueRelation;
    }

    public String getTransitionUrl() {
        return transitionUrl;
    }

    public void setTransitionUrl(String transitionUrl) {
        this.transitionUrl = transitionUrl;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
