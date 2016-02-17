package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/8/4.
 */
public class BaoyangBrand extends BaseModelObj {
    @ApiField("id")
    private String id;
    @ApiField("initial")
    private String initial;
    @ApiField("name")
    private String name;
    @ApiField("letter")
    private String letter;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }
}
