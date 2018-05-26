package com.rudranshdigital.hilfie.service;

import com.rudranshdigital.hilfie.domain.Classroom;
import com.rudranshdigital.hilfie.repository.ClassroomRepository;
import com.rudranshdigital.hilfie.repository.search.ClassroomSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Classroom.
 */
@Service
@Transactional
public class ClassroomService {

    private final Logger log = LoggerFactory.getLogger(ClassroomService.class);

    private final ClassroomRepository classroomRepository;

    private final ClassroomSearchRepository classroomSearchRepository;

    public ClassroomService(ClassroomRepository classroomRepository, ClassroomSearchRepository classroomSearchRepository) {
        this.classroomRepository = classroomRepository;
        this.classroomSearchRepository = classroomSearchRepository;
    }

    /**
     * Save a classroom.
     *
     * @param classroom the entity to save
     * @return the persisted entity
     */
    public Classroom save(Classroom classroom) {
        log.debug("Request to save Classroom : {}", classroom);
        Classroom result = classroomRepository.save(classroom);
        classroomSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the classrooms.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Classroom> findAll(Pageable pageable) {
        log.debug("Request to get all Classrooms");
        return classroomRepository.findAll(pageable);
    }

    /**
     * Get one classroom by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Classroom findOne(Long id) {
        log.debug("Request to get Classroom : {}", id);
        return classroomRepository.findOne(id);
    }

    /**
     * Delete the classroom by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Classroom : {}", id);
        classroomRepository.delete(id);
        classroomSearchRepository.delete(id);
    }

    /**
     * Search for the classroom corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Classroom> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Classrooms for query {}", query);
        Page<Classroom> result = classroomSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
