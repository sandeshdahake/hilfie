package com.rudranshdigital.hilfie.service;

import com.rudranshdigital.hilfie.domain.User;
import com.rudranshdigital.hilfie.domain.UserProfile;
import com.rudranshdigital.hilfie.repository.UserProfileRepository;
import com.rudranshdigital.hilfie.repository.search.UserProfileSearchRepository;
import com.rudranshdigital.hilfie.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing UserProfile.
 */
@Service
@Transactional
public class UserProfileService {

    private final Logger log = LoggerFactory.getLogger(UserProfileService.class);

    private final UserProfileRepository userProfileRepository;

    private final UserProfileSearchRepository userProfileSearchRepository;
    private final UserService userService;

    public UserProfileService(UserProfileRepository userProfileRepository, UserProfileSearchRepository userProfileSearchRepository,
    UserService userService) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileSearchRepository = userProfileSearchRepository;
        this.userService = userService;

    }

    /**
     * Save a userProfile.
     *
     * @param userProfile the entity to save
     * @return the persisted entity
     */
    public UserProfile save(UserProfile userProfile) {
        log.debug("Request to save UserProfile : {}", userProfile);
        UserProfile result = userProfileRepository.save(userProfile);
        userProfileSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the userProfiles.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UserProfile> findAll(Pageable pageable) {
        log.debug("Request to get all UserProfiles");
        Optional<String> currentUser = SecurityUtils.getCurrentUserLogin();
        final Optional<User> isUser = userService.getUserWithAuthorities();
        if(!isUser.isPresent()) {
            log.error("User is not logged in");
            return null;
        }
        final User user = isUser.get();
        UserProfile userProfile=findOne(user.getLogin());
       // return userProfileRepository.findAllBySchool(pageable,userProfile.getSchool());
         return userProfileRepository.findAll(pageable);

    }

    /**
     * Get one userProfile by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public UserProfile findOne(Long id) {
        log.debug("Request to get UserProfile : {}", id);
        return userProfileRepository.findOne(id);
    }

    /**
     * Delete the userProfile by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete UserProfile : {}", id);
        userProfileRepository.delete(id);
        userProfileSearchRepository.delete(id);
    }

    /**
     * Search for the userProfile corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UserProfile> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserProfiles for query {}", query);
        Page<UserProfile> result = userProfileSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

    /**
     * Get one userProfile by id.
     *
     * @param login the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public UserProfile findOne(String login) {
        log.debug("Request to get UserProfile : {}", login);
        return userProfileRepository.findByUserLogin(login);
    }

}
