package com.selecthome.service.impl;

import com.selecthome.repository.HouseRepository;
import com.selecthome.service.HouseIndexAsyncNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@ConditionalOnProperty(prefix = "app.dev", name = "init-elasticsearch", havingValue = "true")
public class ElasticSearchInitService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchInitService.class);
    private final HouseRepository houseRepository;
    private final HouseIndexAsyncNotifier notifier;

    public ElasticSearchInitService(HouseRepository houseRepository, HouseIndexAsyncNotifier notifier) {
        this.houseRepository = houseRepository;
        this.notifier = notifier;
    }

    @PostConstruct
    public void syncIndex() {
        houseRepository.findAll().forEach(house -> {
            notifier.notify(house);
            LOGGER.info("Successfully to sync index for house: {}", house.getId());
        });
    }
}
