package com.selecthome.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class SupportAddress {
    @Id
    @GeneratedValue
    private Long id;
    private String belongTO;
    private String enName;
    private String cnName;
    private String level;
    private Double baiduMapIng;
    private Double baiduMapLat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBelongTO() {
        return belongTO;
    }

    public void setBelongTO(String belongTO) {
        this.belongTO = belongTO;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Double getBaiduMapIng() {
        return baiduMapIng;
    }

    public void setBaiduMapIng(Double baiduMapIng) {
        this.baiduMapIng = baiduMapIng;
    }

    public Double getBaiduMapLat() {
        return baiduMapLat;
    }

    public void setBaiduMapLat(Double baiduMapLat) {
        this.baiduMapLat = baiduMapLat;
    }
}
