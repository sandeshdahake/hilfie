package com.rudranshdigital.hilfie.repository.search;

import com.rudranshdigital.hilfie.domain.Classroom;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Classroom entity.
 */
public interface ClassroomSearchRepository extends ElasticsearchRepository<Classroom, Long> {
}
