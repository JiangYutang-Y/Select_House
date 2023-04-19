package com.Select_Home.Repository;

import com.Select_Home.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    //应用启动时：生成实例类
    //找方法名
    //find  -> select id,name ,...from user // user 是从UserRepository 上的泛型去分析的
    //by -> where
    //Name ->name = ?
    //分析参数
    //需要一个参数，顺序从方法的参数中拿，拿到第一个时 String name, 自动补充到对应占位符上去
    //分析返回值
    // 看返回值类型，如果是 List ，Set ，Page，则继续查看类里面的泛型参数是什么，实例化相应的集合，进行第三步
    //返回值如果与Repository 上泛型相对应，则可以继续第三步，
    // 将select 出来的结果<Result> 与对象的属性匹配并自动设置进去
    //上述过程会在应用运行的过程中自动化处理
    User findByName(String name);


    User findByEmail(String email);

    User findByPhoneNumber(String phoneNumber);



}
