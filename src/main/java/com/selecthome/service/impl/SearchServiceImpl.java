package com.selecthome.service.impl;

import com.selecthome.base.BusinessException;
import com.selecthome.base.Status;
import com.selecthome.dto.HouseDTO;
import com.selecthome.dto.SearchParams;
import com.selecthome.entity.House;
import com.selecthome.entity.es.HouseDocument;
import com.selecthome.repository.HouseRepository;
import com.selecthome.repository.es.HouseDocumentRepository;
import com.selecthome.service.SearchService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.suggest.Completion;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
    private final ElasticsearchRestTemplate template;
    private final HouseDocumentRepository repository;
    private final HouseRepository houseRepository;

    public SearchServiceImpl(ElasticsearchRestTemplate template, HouseDocumentRepository repository, HouseRepository houseRepository) {
        this.template = template;
        this.repository = repository;
        this.houseRepository = houseRepository;
    }

    @Override
    public List<String> autocomplete(String prefix) {
        // 自动完成建议的构造，使用 suggest 字段，使用 prefix 前缀匹配，去重并限制最多返回5个结果
        CompletionSuggestionBuilder builder = SuggestBuilders.completionSuggestion("suggest").prefix(prefix)
                .skipDuplicates(true).size(5);
        SuggestBuilder sb = new SuggestBuilder();
        // 增加建议项，其中 autocomplete 是键名，可以任意指定
        sb.addSuggestion("autocomplete", builder);
        // 构建原生查询语句，只有这种原生的查询才支持 suggest，非原生的暂时不支持，非原生指 CriteriaQuery
        NativeSearchQuery query = new NativeSearchQueryBuilder().withSuggestBuilder(sb).build();
        // 调用 template search 统一的搜索接口
        SearchHits<HouseDocument> result = template.search(query, HouseDocument.class);
        // SearchHits 是 elasticsearch 的统一响应，类比 ApiResponse。
        // getSuggest 获取建议结果
        Suggest suggest = result.getSuggest();
        // 从建议结果里获取所有的建议项
        // {
        //    "entries": [
        //        {
        //           "text": "xxx"
        //        }
        //        {
        //           "text": "xxx2"
        //        }
        //    ]
        // }
        return suggest.getSuggestion("autocomplete").getEntries().stream()
                .flatMap(e -> e.getOptions().stream()).map(Suggest.Suggestion.Entry.Option::getText).collect(Collectors.toList());
    }

    @Override
    public void updateIndex(House house) {
        HouseDocument document = new HouseDocument();
        BeanUtils.copyProperties(house, document);
        Completion completion = new Completion();
        completion.setInput(new String[]{document.getTitle(), document.getCityEnName(), document.getRegionEnName()});
        document.setSuggest(completion);
        repository.save(document);
    }

    @Override
    public Page<HouseDTO> search(SearchParams params) {
        // 参数检查
        if (!StringUtils.hasText(params.getCityEnName())) {
            throw new BusinessException(Status.CITY_EN_NAME_INVALID);
        }
        // 如果没有传入关键词，那么直接去数据库拿
        if (!StringUtils.hasText(params.getKeyword())) {
            PageRequest pageable = PageRequest.of(params.getPage(), params.getSize(), Sort.Direction.DESC, "lastUpdateTime");
            Page<House> result = houseRepository.findAllByCityEnNameOrRegionEnName(params.getCityEnName(), params.getRegionEnName(), pageable);
            return new PageImpl<>(result.stream()
                    .map(h -> {
                        HouseDTO dto = new HouseDTO();
                        BeanUtils.copyProperties(h, dto);
                        return dto;
                    }).collect(Collectors.toList()), pageable, result.getTotalElements());
        }
        // 有关键词则去 elasticsearch 里搜索
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        // 城市使用 filter 过滤
        builder.filter(QueryBuilders.termQuery(HouseDocument.KEY_CITI_EN_NAME, params.getCityEnName()));
        // 区域使用 filter 过滤，不过参数可选
        if (StringUtils.hasText(params.getRegionEnName())) {
            builder.filter(QueryBuilders.termQuery(HouseDocument.KEY_REGION_EN_NAME, params.getRegionEnName()));
        }
        // 关键词匹配分词搜索
        // multiMatchQuery 是为了后面能匹配多个字段，当前暂时只有 title ，后续会增加 description 等等。
        builder.must(QueryBuilders.multiMatchQuery(params.getKeyword(), HouseDocument.KEY_TITLE));
        PageRequest pageable = PageRequest.of(params.getPage(), params.getSize());
        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(builder)
                .withPageable(pageable).build();
        SearchHits<HouseDocument> result = template.search(query, HouseDocument.class);
        // 从搜索结果中拿到所有的房源 ID
        List<Long> ids = result.getSearchHits().stream().map(h -> h.getContent().getId()).collect(Collectors.toList());
        // 通过这些房源 ID 去数据库获取真实的房源数据
        List<House> houses = houseRepository.findAllById(ids);
        return new PageImpl<>(houses.stream()
                .map(h -> {
                    HouseDTO dto = new HouseDTO();
                    BeanUtils.copyProperties(h, dto);
                    return dto;
                }).collect(Collectors.toList()), pageable, result.getTotalHits());
    }

}
