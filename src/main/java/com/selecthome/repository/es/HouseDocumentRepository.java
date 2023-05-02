package com.selecthome.repository.es;

import com.selecthome.entity.es.HouseDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface HouseDocumentRepository extends ElasticsearchRepository<HouseDocument, Long> {
}
