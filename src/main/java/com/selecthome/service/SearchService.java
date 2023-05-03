package com.selecthome.service;

import com.selecthome.dto.HouseDTO;
import com.selecthome.dto.SearchParams;
import com.selecthome.entity.House;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SearchService {
    List<String> autocomplete(String prefix);
    void updateIndex(House house);

    Page<HouseDTO> search(SearchParams params);
}
