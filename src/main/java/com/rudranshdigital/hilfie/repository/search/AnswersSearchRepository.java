package com.rudranshdigital.hilfie.repository.search;

import com.rudranshdigital.hilfie.domain.Answers;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Answers entity.
 */
public interface AnswersSearchRepository extends ElasticsearchRepository<Answers, Long> {
}
