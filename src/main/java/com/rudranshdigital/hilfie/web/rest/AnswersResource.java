package com.rudranshdigital.hilfie.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.rudranshdigital.hilfie.domain.Answers;
import com.rudranshdigital.hilfie.service.AnswersService;
import com.rudranshdigital.hilfie.web.rest.errors.BadRequestAlertException;
import com.rudranshdigital.hilfie.web.rest.util.HeaderUtil;
import com.rudranshdigital.hilfie.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Answers.
 */
@RestController
@RequestMapping("/api")
public class AnswersResource {

    private final Logger log = LoggerFactory.getLogger(AnswersResource.class);

    private static final String ENTITY_NAME = "answers";

    private final AnswersService answersService;

    public AnswersResource(AnswersService answersService) {
        this.answersService = answersService;
    }

    /**
     * POST  /answers : Create a new answers.
     *
     * @param answers the answers to create
     * @return the ResponseEntity with status 201 (Created) and with body the new answers, or with status 400 (Bad Request) if the answers has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/answers")
    @Timed
    public ResponseEntity<Answers> createAnswers(@Valid @RequestBody Answers answers) throws URISyntaxException {
        log.debug("REST request to save Answers : {}", answers);
        if (answers.getId() != null) {
            throw new BadRequestAlertException("A new answers cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Answers result = answersService.save(answers);
        return ResponseEntity.created(new URI("/api/answers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /answers : Updates an existing answers.
     *
     * @param answers the answers to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated answers,
     * or with status 400 (Bad Request) if the answers is not valid,
     * or with status 500 (Internal Server Error) if the answers couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/answers")
    @Timed
    public ResponseEntity<Answers> updateAnswers(@Valid @RequestBody Answers answers) throws URISyntaxException {
        log.debug("REST request to update Answers : {}", answers);
        if (answers.getId() == null) {
            return createAnswers(answers);
        }
        Answers result = answersService.save(answers);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, answers.getId().toString()))
            .body(result);
    }

    /**
     * GET  /answers : get all the answers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of answers in body
     */
    @GetMapping("/answers")
    @Timed
    public ResponseEntity<List<Answers>> getAllAnswers(Pageable pageable) {
        log.debug("REST request to get a page of Answers");
        Page<Answers> page = answersService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/answers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /answers/:id : get the "id" answers.
     *
     * @param id the id of the answers to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the answers, or with status 404 (Not Found)
     */
    @GetMapping("/answers/{id}")
    @Timed
    public ResponseEntity<Answers> getAnswers(@PathVariable Long id) {
        log.debug("REST request to get Answers : {}", id);
        Answers answers = answersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(answers));
    }

    /**
     * DELETE  /answers/:id : delete the "id" answers.
     *
     * @param id the id of the answers to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/answers/{id}")
    @Timed
    public ResponseEntity<Void> deleteAnswers(@PathVariable Long id) {
        log.debug("REST request to delete Answers : {}", id);
        answersService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/answers?query=:query : search for the answers corresponding
     * to the query.
     *
     * @param query the query of the answers search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/answers")
    @Timed
    public ResponseEntity<List<Answers>> searchAnswers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Answers for query {}", query);
        Page<Answers> page = answersService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/answers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
