package com.rudranshdigital.hilfie.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.rudranshdigital.hilfie.domain.Classroom;
import com.rudranshdigital.hilfie.domain.School;
import com.rudranshdigital.hilfie.domain.User;
import com.rudranshdigital.hilfie.domain.UserProfile;
import com.rudranshdigital.hilfie.service.ClassroomService;
import com.rudranshdigital.hilfie.service.SchoolService;
import com.rudranshdigital.hilfie.service.UserProfileService;
import com.rudranshdigital.hilfie.service.UserService;
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

/**
 * REST controller for managing Classroom.
 */
@RestController
@RequestMapping("/api")
public class ClassroomResource {

    private final Logger log = LoggerFactory.getLogger(ClassroomResource.class);

    private static final String ENTITY_NAME = "classroom";

    private final ClassroomService classroomService;
    private final UserService userService;
    private final SchoolService schoolService;
    private final UserProfileService userProfileService;

    public ClassroomResource(ClassroomService classroomService, UserService userService, SchoolService schoolService, UserProfileService userProfileService) {
        this.classroomService = classroomService;
        this.userService = userService;
        this.schoolService = schoolService;
        this.userProfileService = userProfileService;
    }

    /**
     * POST  /classrooms : Create a new classroom.
     *
     * @param classroom the classroom to create
     * @return the ResponseEntity with status 201 (Created) and with body the new classroom, or with status 400 (Bad Request) if the classroom has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/classrooms")
    @Timed
    public ResponseEntity<Classroom> createClassroom(@Valid @RequestBody Classroom classroom) throws URISyntaxException {
        log.debug("REST request to save Classroom : {}", classroom);
        if (classroom.getId() != null) {
            throw new BadRequestAlertException("A new classroom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Classroom result = classroomService.save(classroom);
        return ResponseEntity.created(new URI("/api/classrooms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /classrooms : Updates an existing classroom.
     *
     * @param classroom the classroom to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated classroom,
     * or with status 400 (Bad Request) if the classroom is not valid,
     * or with status 500 (Internal Server Error) if the classroom couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/classrooms")
    @Timed
    public ResponseEntity<Classroom> updateClassroom(@Valid @RequestBody Classroom classroom) throws URISyntaxException {
        log.debug("REST request to update Classroom : {}", classroom);
        if (classroom.getId() == null) {
            return createClassroom(classroom);
        }
        Classroom result = classroomService.save(classroom);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, classroom.getId().toString()))
            .body(result);
    }

    /**
     * GET  /classrooms : get all the classrooms.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of classrooms in body
     */
    @GetMapping("/classrooms")
    @Timed
    public ResponseEntity<List<Classroom>> getAllClassrooms(Pageable pageable) {
        log.debug("REST request to get a page of Classrooms");
        User user = userService.getUserWithAuthorities().get();
        UserProfile userProfile = userProfileService.findOne(user.getLogin());
        School school = userProfile.getSchool();
        Page<Classroom> page;
        if(school == null){
             page = classroomService.findAll(pageable);
        }else{
             page = classroomService.findAllBySchoolName(pageable, school);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/classrooms");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /classrooms/:id : get the "id" classroom.
     *
     * @param id the id of the classroom to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the classroom, or with status 404 (Not Found)
     */
    @GetMapping("/classrooms/{id}")
    @Timed
    public ResponseEntity<Classroom> getClassroom(@PathVariable Long id) {
        log.debug("REST request to get Classroom : {}", id);
        Classroom classroom = classroomService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(classroom));
    }

    /**
     * DELETE  /classrooms/:id : delete the "id" classroom.
     *
     * @param id the id of the classroom to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/classrooms/{id}")
    @Timed
    public ResponseEntity<Void> deleteClassroom(@PathVariable Long id) {
        log.debug("REST request to delete Classroom : {}", id);
        classroomService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/classrooms?query=:query : search for the classroom corresponding
     * to the query.
     *
     * @param query the query of the classroom search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/classrooms")
    @Timed
    public ResponseEntity<List<Classroom>> searchClassrooms(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Classrooms for query {}", query);
        Page<Classroom> page = classroomService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/classrooms");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
