package com.rudranshdigital.hilfie.service;

import com.rudranshdigital.hilfie.domain.School;
import com.rudranshdigital.hilfie.repository.SchoolRepository;
import com.rudranshdigital.hilfie.repository.search.SchoolSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing School.
 */
@Service
@Transactional
public class SchoolService {

    private final Logger log = LoggerFactory.getLogger(SchoolService.class);

    private final SchoolRepository schoolRepository;

    private final SchoolSearchRepository schoolSearchRepository;

    public SchoolService(SchoolRepository schoolRepository, SchoolSearchRepository schoolSearchRepository) {
        this.schoolRepository = schoolRepository;
        this.schoolSearchRepository = schoolSearchRepository;
    }

    /**
     * Save a school.
     *
     * @param school the entity to save
     * @return the persisted entity
     */
    public School save(School school) {
        log.debug("Request to save School : {}", school);
        School result = schoolRepository.save(school);
        schoolSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the schools.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<School> findAll(Pageable pageable) {
        log.debug("Request to get all Schools");
        return schoolRepository.findAll(pageable);
    }

    /**
     * Get one school by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public School findOne(Long id) {
        log.debug("Request to get School : {}", id);
        return schoolRepository.findOne(id);
    }

    /**
     * Delete the school by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete School : {}", id);
        schoolRepository.delete(id);
        schoolSearchRepository.delete(id);
    }

    /**
     * Search for the school corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<School> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Schools for query {}", query);
        Page<School> result = schoolSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
