package com.selecthome.controller;

import com.selecthome.base.ApiResponse;
import com.selecthome.dto.HouseDTO;
import com.selecthome.dto.SearchParams;
import com.selecthome.service.SearchService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/houses")
public class HouseController {
    private final SearchService searchService;

    public HouseController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/suggestions")
    public ApiResponse<List<String>> autocomplete(@RequestParam("prefix") String prefix) {
        return ApiResponse.success(searchService.autocomplete(prefix));
    }

    @PostMapping("/search")
    public ApiResponse<Page<HouseDTO>> search(@RequestBody SearchParams params) {
        return ApiResponse.success(searchService.search(params));
    }
}
