package com.rudranshdigital.hilfie.web.rest;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.codahale.metrics.annotation.Timed;
import com.rudranshdigital.hilfie.config.ApplicationProperties;
import com.rudranshdigital.hilfie.domain.Questions;
import com.rudranshdigital.hilfie.domain.User;
import com.rudranshdigital.hilfie.service.ClassroomService;
import com.rudranshdigital.hilfie.service.QuestionsService;
import com.rudranshdigital.hilfie.service.UserService;
import com.rudranshdigital.hilfie.web.rest.errors.BadRequestAlertException;
import com.rudranshdigital.hilfie.web.rest.util.HeaderUtil;
import com.rudranshdigital.hilfie.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Questions.
 */
@RestController
@RequestMapping("/api")
public class QuestionsResource {

    private final Logger log = LoggerFactory.getLogger(QuestionsResource.class);

    private static final String ENTITY_NAME = "questions";

    private final QuestionsService questionsService;
    private final UserService userService;
    private final ClassroomService classroomService;
    @Autowired
    ApplicationProperties applicationProperties;

    /*
    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
        "cloud_name", "sandeshdahake",
        "api_key", "864573788265325",
        "api_secret", "LhNNz1wV8YcLY9OrO5adlU3We4E"));
*/

    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
        "cloud_name", "sandeshdahake",
        "api_key", "864573788265325",
        "api_secret", "LhNNz1wV8YcLY9OrO5adlU3We4E"));

    String uploadPath = "C:\\Work\\Hilfie\\images\\";

    public QuestionsResource(QuestionsService questionsService, UserService userService, ClassroomService classroomService) {
        this.questionsService = questionsService;
        this.userService = userService;
        this.classroomService = classroomService;
    }

    /**
     * POST  /questions : Create a new questions.
     *
     * @param questions the questions to create
     * @return the ResponseEntity with status 201 (Created) and with body the new questions, or with status 400 (Bad Request) if the questions has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/questions")
    @Timed
    public ResponseEntity<Questions> createQuestions(@RequestBody Questions questions) throws URISyntaxException {
        log.debug("REST request to save Questions : {}", questions);
        if (questions.getId() != null) {
            throw new BadRequestAlertException("A new questions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        User user = userService.getUserWithAuthorities().get();
        questions.setUser(user);
        questions.setDateCreated(LocalDate.now());
        questions.setDateUpdated(LocalDate.now());
        questions.setActive(true);
        questions.setAnswerCount(0);
        questions.setIsAnonymous(false);
/*
        questions.setUser();
        questions.setClassroom();
*/
        Questions result = questionsService.save(questions);
        return ResponseEntity.created(new URI("/api/questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /questions : Updates an existing questions.
     *
     * @param questions the questions to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated questions,
     * or with status 400 (Bad Request) if the questions is not valid,
     * or with status 500 (Internal Server Error) if the questions couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/questions")
    @Timed
    public ResponseEntity<Questions> updateQuestions(@Valid @RequestBody Questions questions) throws URISyntaxException {
        log.debug("REST request to update Questions : {}", questions);
        if (questions.getId() == null) {
            return createQuestions(questions);
        }
        questions.setDateUpdated(LocalDate.now());
        Questions result = questionsService.save(questions);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, questions.getId().toString()))
            .body(result);
    }

    /**
     * GET  /questions : get all the questions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of questions in body
     */
    @GetMapping("/questions")
    @Timed
    public ResponseEntity<List<Questions>> getAllQuestions(Pageable pageable) {
        log.debug("REST request to get a page of Questions");
        Page<Questions> page = questionsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/questions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /questions/:id : get the "id" questions.
     *
     * @param id the id of the questions to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the questions, or with status 404 (Not Found)
     */
    @GetMapping("/questions/{id}")
    @Timed
    public ResponseEntity<Questions> getQuestions(@PathVariable Long id) {
        log.debug("REST request to get Questions : {}", id);
        Questions questions = questionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(questions));
    }

    /**
     * DELETE  /questions/:id : delete the "id" questions.
     *
     * @param id the id of the questions to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/questions/{id}")
    @Timed
    public ResponseEntity<Void> deleteQuestions(@PathVariable Long id) {
        log.debug("REST request to delete Questions : {}", id);
        questionsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/questions?query=:query : search for the questions corresponding
     * to the query.
     *
     * @param query the query of the questions search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/questions")
    @Timed
    public ResponseEntity<List<Questions>> searchQuestions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Questions for query {}", query);
        Page<Questions> page = questionsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/questions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping(value = "/questions/imageUpload",produces = { "text/plain" })
    @Timed
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws URISyntaxException, IOException {
         Path rootLocation = Paths.get(applicationProperties.getImageUploadPath());

        log.debug("REST request to upload image : {}", file);
        String fileName = Math.random()+ file.getName();
        Files.copy(file.getInputStream(), rootLocation.resolve(fileName));
        Map uploadResult = cloudinary.uploader().upload(new File(applicationProperties.getImageUploadPath()+fileName), ObjectUtils.emptyMap());
       // Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());

        String imageUrl = (String)uploadResult.get("url");
        return ResponseEntity.ok().body(imageUrl);
    }

}
