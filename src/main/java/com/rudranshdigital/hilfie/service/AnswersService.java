package com.rudranshdigital.hilfie.service;

import com.rudranshdigital.hilfie.domain.Answers;
import com.rudranshdigital.hilfie.repository.AnswersRepository;
import com.rudranshdigital.hilfie.repository.search.AnswersSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Answers.
 */
@Service
@Transactional
public class AnswersService {

    private final Logger log = LoggerFactory.getLogger(AnswersService.class);

    private final AnswersRepository answersRepository;

    private final AnswersSearchRepository answersSearchRepository;

    public AnswersService(AnswersRepository answersRepository, AnswersSearchRepository answersSearchRepository) {
        this.answersRepository = answersRepository;
        this.answersSearchRepository = answersSearchRepository;
    }

    /**
     * Save a answers.
     *
     * @param answers the entity to save
     * @return the persisted entity
     */
    public Answers save(Answers answers) {
        log.debug("Request to save Answers : {}", answers);
        Answers result = answersRepository.save(answers);
        answersSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the answers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Answers> findAll(Pageable pageable) {
        log.debug("Request to get all Answers");
        return answersRepository.findAll(pageable);
    }

    /**
     * Get one answers by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Answers findOne(Long id) {
        log.debug("Request to get Answers : {}", id);
        return answersRepository.findOne(id);
    }

    /**
     * Delete the answers by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Answers : {}", id);
        answersRepository.delete(id);
        answersSearchRepository.delete(id);
    }

    /**
     * Search for the answers corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Answers> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Answers for query {}", query);
        Page<Answers> result = answersSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
