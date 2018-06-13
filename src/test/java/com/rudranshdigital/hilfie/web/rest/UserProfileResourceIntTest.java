package com.rudranshdigital.hilfie.web.rest;

import com.rudranshdigital.hilfie.HilfieApp;

import com.rudranshdigital.hilfie.domain.UserProfile;
import com.rudranshdigital.hilfie.domain.User;
import com.rudranshdigital.hilfie.domain.School;
import com.rudranshdigital.hilfie.repository.UserProfileRepository;
import com.rudranshdigital.hilfie.service.UserProfileService;
import com.rudranshdigital.hilfie.repository.search.UserProfileSearchRepository;
import com.rudranshdigital.hilfie.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.rudranshdigital.hilfie.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserProfileResource REST controller.
 *
 * @see UserProfileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HilfieApp.class)
public class UserProfileResourceIntTest {

    private static final String DEFAULT_USER_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_USER_PHONE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_USER_DOB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_USER_DOB = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_USER_BLOOD_GROUP = "AAAAAAAAAA";
    private static final String UPDATED_USER_BLOOD_GROUP = "BBBBBBBBBB";

    private static final String DEFAULT_USER_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_USER_IMAGE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVATE = false;
    private static final Boolean UPDATED_ACTIVATE = true;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserProfileSearchRepository userProfileSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserProfileMockMvc;

    private UserProfile userProfile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserProfileResource userProfileResource = new UserProfileResource(userProfileService);
        this.restUserProfileMockMvc = MockMvcBuilders.standaloneSetup(userProfileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProfile createEntity(EntityManager em) {
        UserProfile userProfile = new UserProfile()
            .userPhone(DEFAULT_USER_PHONE)
            .userDob(DEFAULT_USER_DOB)
            .userBloodGroup(DEFAULT_USER_BLOOD_GROUP)
            .userImage(DEFAULT_USER_IMAGE)
            .activate(DEFAULT_ACTIVATE);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        userProfile.setUser(user);
        // Add required entity
        School school = SchoolResourceIntTest.createEntity(em);
        em.persist(school);
        em.flush();
        userProfile.setSchool(school);
        return userProfile;
    }

    @Before
    public void initTest() {
        userProfileSearchRepository.deleteAll();
        userProfile = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserProfile() throws Exception {
        int databaseSizeBeforeCreate = userProfileRepository.findAll().size();

        // Create the UserProfile
        restUserProfileMockMvc.perform(post("/api/user-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userProfile)))
            .andExpect(status().isCreated());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeCreate + 1);
        UserProfile testUserProfile = userProfileList.get(userProfileList.size() - 1);
        assertThat(testUserProfile.getUserPhone()).isEqualTo(DEFAULT_USER_PHONE);
        assertThat(testUserProfile.getUserDob()).isEqualTo(DEFAULT_USER_DOB);
        assertThat(testUserProfile.getUserBloodGroup()).isEqualTo(DEFAULT_USER_BLOOD_GROUP);
        assertThat(testUserProfile.getUserImage()).isEqualTo(DEFAULT_USER_IMAGE);
        assertThat(testUserProfile.isActivate()).isEqualTo(DEFAULT_ACTIVATE);

        // Validate the UserProfile in Elasticsearch
        UserProfile userProfileEs = userProfileSearchRepository.findOne(testUserProfile.getId());
        assertThat(userProfileEs).isEqualToIgnoringGivenFields(testUserProfile);
    }

    @Test
    @Transactional
    public void createUserProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userProfileRepository.findAll().size();

        // Create the UserProfile with an existing ID
        userProfile.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserProfileMockMvc.perform(post("/api/user-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userProfile)))
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkActivateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userProfileRepository.findAll().size();
        // set the field null
        userProfile.setActivate(null);

        // Create the UserProfile, which fails.

        restUserProfileMockMvc.perform(post("/api/user-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userProfile)))
            .andExpect(status().isBadRequest());

        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserProfiles() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList
        restUserProfileMockMvc.perform(get("/api/user-profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].userPhone").value(hasItem(DEFAULT_USER_PHONE.toString())))
            .andExpect(jsonPath("$.[*].userDob").value(hasItem(DEFAULT_USER_DOB.toString())))
            .andExpect(jsonPath("$.[*].userBloodGroup").value(hasItem(DEFAULT_USER_BLOOD_GROUP.toString())))
            .andExpect(jsonPath("$.[*].userImage").value(hasItem(DEFAULT_USER_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].activate").value(hasItem(DEFAULT_ACTIVATE.booleanValue())));
    }

    @Test
    @Transactional
    public void getUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get the userProfile
        restUserProfileMockMvc.perform(get("/api/user-profiles/{id}", userProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userProfile.getId().intValue()))
            .andExpect(jsonPath("$.userPhone").value(DEFAULT_USER_PHONE.toString()))
            .andExpect(jsonPath("$.userDob").value(DEFAULT_USER_DOB.toString()))
            .andExpect(jsonPath("$.userBloodGroup").value(DEFAULT_USER_BLOOD_GROUP.toString()))
            .andExpect(jsonPath("$.userImage").value(DEFAULT_USER_IMAGE.toString()))
            .andExpect(jsonPath("$.activate").value(DEFAULT_ACTIVATE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUserProfile() throws Exception {
        // Get the userProfile
        restUserProfileMockMvc.perform(get("/api/user-profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserProfile() throws Exception {
        // Initialize the database
        userProfileService.save(userProfile);

        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();

        // Update the userProfile
        UserProfile updatedUserProfile = userProfileRepository.findOne(userProfile.getId());
        // Disconnect from session so that the updates on updatedUserProfile are not directly saved in db
        em.detach(updatedUserProfile);
        updatedUserProfile
            .userPhone(UPDATED_USER_PHONE)
            .userDob(UPDATED_USER_DOB)
            .userBloodGroup(UPDATED_USER_BLOOD_GROUP)
            .userImage(UPDATED_USER_IMAGE)
            .activate(UPDATED_ACTIVATE);

        restUserProfileMockMvc.perform(put("/api/user-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserProfile)))
            .andExpect(status().isOk());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
        UserProfile testUserProfile = userProfileList.get(userProfileList.size() - 1);
        assertThat(testUserProfile.getUserPhone()).isEqualTo(UPDATED_USER_PHONE);
        assertThat(testUserProfile.getUserDob()).isEqualTo(UPDATED_USER_DOB);
        assertThat(testUserProfile.getUserBloodGroup()).isEqualTo(UPDATED_USER_BLOOD_GROUP);
        assertThat(testUserProfile.getUserImage()).isEqualTo(UPDATED_USER_IMAGE);
        assertThat(testUserProfile.isActivate()).isEqualTo(UPDATED_ACTIVATE);

        // Validate the UserProfile in Elasticsearch
        UserProfile userProfileEs = userProfileSearchRepository.findOne(testUserProfile.getId());
        assertThat(userProfileEs).isEqualToIgnoringGivenFields(testUserProfile);
    }

    @Test
    @Transactional
    public void updateNonExistingUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();

        // Create the UserProfile

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserProfileMockMvc.perform(put("/api/user-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userProfile)))
            .andExpect(status().isCreated());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserProfile() throws Exception {
        // Initialize the database
        userProfileService.save(userProfile);

        int databaseSizeBeforeDelete = userProfileRepository.findAll().size();

        // Get the userProfile
        restUserProfileMockMvc.perform(delete("/api/user-profiles/{id}", userProfile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean userProfileExistsInEs = userProfileSearchRepository.exists(userProfile.getId());
        assertThat(userProfileExistsInEs).isFalse();

        // Validate the database is empty
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchUserProfile() throws Exception {
        // Initialize the database
        userProfileService.save(userProfile);

        // Search the userProfile
        restUserProfileMockMvc.perform(get("/api/_search/user-profiles?query=id:" + userProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].userPhone").value(hasItem(DEFAULT_USER_PHONE.toString())))
            .andExpect(jsonPath("$.[*].userDob").value(hasItem(DEFAULT_USER_DOB.toString())))
            .andExpect(jsonPath("$.[*].userBloodGroup").value(hasItem(DEFAULT_USER_BLOOD_GROUP.toString())))
            .andExpect(jsonPath("$.[*].userImage").value(hasItem(DEFAULT_USER_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].activate").value(hasItem(DEFAULT_ACTIVATE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProfile.class);
        UserProfile userProfile1 = new UserProfile();
        userProfile1.setId(1L);
        UserProfile userProfile2 = new UserProfile();
        userProfile2.setId(userProfile1.getId());
        assertThat(userProfile1).isEqualTo(userProfile2);
        userProfile2.setId(2L);
        assertThat(userProfile1).isNotEqualTo(userProfile2);
        userProfile1.setId(null);
        assertThat(userProfile1).isNotEqualTo(userProfile2);
    }
}
