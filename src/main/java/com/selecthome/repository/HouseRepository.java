package com.selecthome.repository;

import com.selecthome.entity.House;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HouseRepository extends JpaRepository<House, Long> {
    @Query("select h from House h where h.cityEnName = :cityEnName and (:regionEnName is null or :regionEnName = '' or h.regionEnName = :regionEnName)")
    Page<House> findAllByCityEnNameOrRegionEnName(String cityEnName, String regionEnName, Pageable pageable);

}
