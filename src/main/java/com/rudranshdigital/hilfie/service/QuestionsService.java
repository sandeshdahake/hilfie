package com.rudranshdigital.hilfie.service;

import com.rudranshdigital.hilfie.domain.Questions;
import com.rudranshdigital.hilfie.repository.QuestionsRepository;
import com.rudranshdigital.hilfie.repository.search.QuestionsSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Questions.
 */
@Service
@Transactional
public class QuestionsService {

    private final Logger log = LoggerFactory.getLogger(QuestionsService.class);

    private final QuestionsRepository questionsRepository;

    private final QuestionsSearchRepository questionsSearchRepository;

    public QuestionsService(QuestionsRepository questionsRepository, QuestionsSearchRepository questionsSearchRepository) {
        this.questionsRepository = questionsRepository;
        this.questionsSearchRepository = questionsSearchRepository;
    }

    /**
     * Save a questions.
     *
     * @param questions the entity to save
     * @return the persisted entity
     */
    public Questions save(Questions questions) {
        log.debug("Request to save Questions : {}", questions);
        Questions result = questionsRepository.save(questions);
        questionsSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the questions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Questions> findAll(Pageable pageable) {
        log.debug("Request to get all Questions");
        return questionsRepository.findAll(pageable);
    }

    /**
     * Get one questions by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Questions findOne(Long id) {
        log.debug("Request to get Questions : {}", id);
        return questionsRepository.findOne(id);
    }

    /**
     * Delete the questions by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Questions : {}", id);
        questionsRepository.delete(id);
        questionsSearchRepository.delete(id);
    }

    /**
     * Search for the questions corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Questions> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Questions for query {}", query);
        Page<Questions> result = questionsSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
