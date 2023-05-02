package com.selecthome.service;

import com.selecthome.entity.House;

import java.util.List;

public interface SearchService {
    List<String> autocomplete(String prefix);
    void updateIndex(House house);
}
