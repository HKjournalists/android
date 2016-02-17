package com.kplus.car.carwash.bean;

import java.sql.Timestamp;

/**
 * Created by Fu on 2015/5/12.
 */
public class CarModel extends BaseInfo {
    private long id;
    private String name;
    private String py;
    private int level;
    private String image;
    private String classify;
    private int weight;
    private int status;
    private CarBrand brand;
    private Timestamp updateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPy() {
        return py;
    }

    public void setPy(String py) {
        this.py = py;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public CarBrand getBrand() {
        return brand;
    }

    public void setBrand(CarBrand brand) {
        this.brand = brand;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
