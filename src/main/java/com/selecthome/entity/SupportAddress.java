package com.selecthome.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SupportAddress {
    @Id
    private Integer id;
    private String belongTo;
    private String enName;
    private String cnName;
    private String level;
    private Double baiduMapLng;
    private Double baiduMapLat;

    public Integer getId() {
        return id;
    }

    public String getBelongTo() {
        return belongTo;
    }

    public String getEnName() {
        return enName;
    }

    public String getCnName() {
        return cnName;
    }

    public String getLevel() {
        return level;
    }

    public Double getBaiduMapLng() {
        return baiduMapLng;
    }

    public Double getBaiduMapLat() {
        return baiduMapLat;
    }
}
