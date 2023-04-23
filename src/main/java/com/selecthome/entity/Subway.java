package com.selecthome.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//声明实体
@Entity  //Subway -> subway ORM框架按照规则映射 ，1.类型首字母自动小写，2.驼峰式命名转下划线命名
//与框架无关，场景：spring jpa 有致命漏洞，长时间无法修复，我们可以直接切一个ORM框架而无需或少量改动代码，从而实现解耦
// POJO实体
public class Subway {
    @Id //声明主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) //数据库自动生成值，依赖数据库主键自增
    private  Long id;
    private String name;
    private String cityName;

//    private  String tmp;//逻辑上处理，连name和city_name

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

    public String getCity_name() {
        return cityName;
    }

    public void setCity_name(String city_name) {
        this.cityName = city_name;
    }
}
