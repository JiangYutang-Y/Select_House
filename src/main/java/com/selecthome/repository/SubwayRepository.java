package com.selecthome.repository;

import com.selecthome.entity.Subway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// dao 层。
// jdbc
// Class.forName
// statement query
// sql
// get result
// result index
// set properties
// 异常处理
// 关闭链接

public interface SubwayRepository extends JpaRepository<Subway,Long> {
    // 查看继承自 Repository 接口的接口里面的方法
    // findByCityEnName
    // find -> select id,city_en_name,name
    // By -> where
    // CityEnName -> city_en_name = ?

    // select * from subway where city = ?

    List<Subway> findByCityEnName(String cityEnName);

    // findByCityEnNameAndName
    // find -> select id,city_en_name,name
    // By -> where
    // CityEnName -> city_en_name = ?
    // And -> and
    // Name -> name = ?
    // ? 按顺序组织，将参数依次传入

    // 返回值使用实体类 -> new 实体类，并将属性塞进去
    // 返回值还支持集合 ： List,Set -> new 集合，并将所有的行 都会塞进集合里。
    //    List<Subway> findByCityEnNameAndName(String city, String name);
    //
    Subway findByCityEnNameAndName(String cityS, String name);


    // 返回值还支持 Page -> 根据参数（必要）进行分页
    // Page<Subway> findAll(Pageable pageable);

}
