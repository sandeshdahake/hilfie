package com.rudranshdigital.hilfie.repository.search;

import com.rudranshdigital.hilfie.domain.Questions;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Questions entity.
 */
public interface QuestionsSearchRepository extends ElasticsearchRepository<Questions, Long> {
}
