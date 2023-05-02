package com.selecthome.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.selecthome.base.HouseMessage;
import com.selecthome.repository.HouseRepository;
import com.selecthome.service.SearchService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class HouseIndexAsyncProcessor {
    private final ObjectMapper mapper;
    private final HouseRepository houseRepository;
    private final SearchService searchService;

    public HouseIndexAsyncProcessor(ObjectMapper mapper, HouseRepository houseRepository, SearchService searchService) {
        this.mapper = mapper;
        this.houseRepository = houseRepository;
        this.searchService = searchService;
    }

    // 从 topic 中接受到消息后会自动调用此方法
    @KafkaListener(topics = HouseMessage.TOPIC)
    public void handleHouseIndex(String content) throws JsonProcessingException {
        HouseMessage message = mapper.readValue(content, HouseMessage.class);
        houseRepository.findById(message.getHouseId()).ifPresent(searchService::updateIndex);
    }
}
