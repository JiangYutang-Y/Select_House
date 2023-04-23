package com.selecthome.repository;

import com.selecthome.entity.SupportAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportAddressRepository extends JpaRepository<SupportAddress,Long> {
    /*List<SupportAddress> findAllByAddress(String add);*/
}
