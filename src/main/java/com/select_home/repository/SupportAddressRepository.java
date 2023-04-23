package com.Select_Home.Repository;

import com.Select_Home.Entity.SupportAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportAddressRepository extends JpaRepository<SupportAddress,Long> {
    /*List<SupportAddress> findAllByAddress(String add);*/
}
