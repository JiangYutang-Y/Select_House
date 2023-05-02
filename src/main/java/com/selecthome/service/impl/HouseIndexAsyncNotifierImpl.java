package com.selecthome.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.selecthome.base.BusinessException;
import com.selecthome.base.HouseMessage;
import com.selecthome.entity.House;
import com.selecthome.service.HouseIndexAsyncNotifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class HouseIndexAsyncNotifierImpl implements HouseIndexAsyncNotifier {
    private final KafkaTemplate<String, String> template;
    private final ObjectMapper mapper;

    public HouseIndexAsyncNotifierImpl(KafkaTemplate<String, String> template, ObjectMapper mapper) {
        this.template = template;
        this.mapper = mapper;
    }

    @Override
    public void notify(House house) {
        String data;
        try {
            data = mapper.writeValueAsString(new HouseMessage(house.getId(), HouseMessage.Type.INDEX));
        } catch (JsonProcessingException e) {
            throw new BusinessException(e);
        }
        template.send(HouseMessage.TOPIC, data);
    }
}
