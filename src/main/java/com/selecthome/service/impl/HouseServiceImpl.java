package com.selecthome.service.impl;

import com.selecthome.dto.HouseParams;
import com.selecthome.entity.House;
import com.selecthome.repository.HouseRepository;
import com.selecthome.service.HouseIndexAsyncNotifier;
import com.selecthome.service.HouseService;
import com.selecthome.utils.IDUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class HouseServiceImpl implements HouseService {
    private final HouseRepository houseRepository;
    private final HouseIndexAsyncNotifier notifier;

    public HouseServiceImpl(HouseRepository houseRepository, HouseIndexAsyncNotifier notifier) {
        this.houseRepository = houseRepository;
        this.notifier = notifier;
    }

    @Override
    public void addHouse(HouseParams params) {
        House house = new House();
        BeanUtils.copyProperties(params, house);
        house.setId(IDUtils.generateId());
        house.setCreateTime(new Date());
        house.setLastUpdateTime(new Date());
        house = houseRepository.save(house);
        notifier.notify(house);
    }
}
