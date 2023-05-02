package com.selecthome.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
//声明实体
@Entity  //Subway -> subway ORM框架按照规则映射 ，1.类型首字母自动小写，2.驼峰式命名转下划线命名
//与框架无关，场景：spring jpa 有致命漏洞，长时间无法修复，我们可以直接切一个ORM框架而无需或少量改动代码，从而实现解耦
// POJO实体
public class Subway {
    @Id //声明主键
    private Long id;
    private String cityEnName;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCityEnName() {
        return cityEnName;
    }

    public void setCityEnName(String cityEnName) {
        this.cityEnName = cityEnName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
