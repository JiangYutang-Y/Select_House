package com.selecthome.controller;

import com.selecthome.base.ApiResponse;
import com.selecthome.service.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
