package com.selecthome.service.impl;

import com.selecthome.entity.House;
import com.selecthome.entity.es.HouseDocument;
import com.selecthome.repository.es.HouseDocumentRepository;
import com.selecthome.service.SearchService;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.suggest.Completion;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
    private final ElasticsearchRestTemplate template;
    private final HouseDocumentRepository repository;

    public SearchServiceImpl(ElasticsearchRestTemplate template, HouseDocumentRepository repository) {
        this.template = template;
        this.repository = repository;
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

}
