package com.rudranshdigital.hilfie.web.rest;

import com.rudranshdigital.hilfie.HilfieApp;

import com.rudranshdigital.hilfie.domain.Answers;
import com.rudranshdigital.hilfie.domain.User;
import com.rudranshdigital.hilfie.domain.Classroom;
import com.rudranshdigital.hilfie.domain.Questions;
import com.rudranshdigital.hilfie.repository.AnswersRepository;
import com.rudranshdigital.hilfie.service.AnswersService;
import com.rudranshdigital.hilfie.repository.search.AnswersSearchRepository;
import com.rudranshdigital.hilfie.service.ClassroomService;
import com.rudranshdigital.hilfie.service.UserService;
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
import org.springframework.util.Base64Utils;

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
 * Test class for the AnswersResource REST controller.
 *
 * @see AnswersResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HilfieApp.class)
public class AnswersResourceIntTest {

    private static final String DEFAULT_ANSWER = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_UPDATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_UPDATED = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_ANONYMOUS = false;
    private static final Boolean UPDATED_IS_ANONYMOUS = true;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private AnswersRepository answersRepository;

    @Autowired
    private AnswersService answersService;
    @Autowired
    private  UserService userService;
    @Autowired
    private  ClassroomService classroomService;

    @Autowired
    private AnswersSearchRepository answersSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAnswersMockMvc;

    private Answers answers;

    public AnswersResourceIntTest() {
        classroomService = null;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AnswersResource answersResource = new AnswersResource(answersService,userService, classroomService );
        this.restAnswersMockMvc = MockMvcBuilders.standaloneSetup(answersResource)
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
    public static Answers createEntity(EntityManager em) {
        Answers answers = new Answers()
            .answer(DEFAULT_ANSWER)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateUpdated(DEFAULT_DATE_UPDATED)
            .isAnonymous(DEFAULT_IS_ANONYMOUS)
            .active(DEFAULT_ACTIVE);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        answers.setUser(user);
        // Add required entity
        Classroom classroom = ClassroomResourceIntTest.createEntity(em);
        em.persist(classroom);
        em.flush();
        answers.setClassroom(classroom);
        // Add required entity
        Questions questions = QuestionsResourceIntTest.createEntity(em);
        em.persist(questions);
        em.flush();
        answers.setQuestions(questions);
        return answers;
    }

    @Before
    public void initTest() {
        answersSearchRepository.deleteAll();
        answers = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnswers() throws Exception {
        int databaseSizeBeforeCreate = answersRepository.findAll().size();

        // Create the Answers
        restAnswersMockMvc.perform(post("/api/answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answers)))
            .andExpect(status().isCreated());

        // Validate the Answers in the database
        List<Answers> answersList = answersRepository.findAll();
        assertThat(answersList).hasSize(databaseSizeBeforeCreate + 1);
        Answers testAnswers = answersList.get(answersList.size() - 1);
        assertThat(testAnswers.getAnswer()).isEqualTo(DEFAULT_ANSWER);
        assertThat(testAnswers.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testAnswers.getDateUpdated()).isEqualTo(DEFAULT_DATE_UPDATED);
        assertThat(testAnswers.isIsAnonymous()).isEqualTo(DEFAULT_IS_ANONYMOUS);
        assertThat(testAnswers.isActive()).isEqualTo(DEFAULT_ACTIVE);

        // Validate the Answers in Elasticsearch
        Answers answersEs = answersSearchRepository.findOne(testAnswers.getId());
        assertThat(answersEs).isEqualToIgnoringGivenFields(testAnswers);
    }

    @Test
    @Transactional
    public void createAnswersWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = answersRepository.findAll().size();

        // Create the Answers with an existing ID
        answers.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnswersMockMvc.perform(post("/api/answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answers)))
            .andExpect(status().isBadRequest());

        // Validate the Answers in the database
        List<Answers> answersList = answersRepository.findAll();
        assertThat(answersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAnswerIsRequired() throws Exception {
        int databaseSizeBeforeTest = answersRepository.findAll().size();
        // set the field null
        answers.setAnswer(null);

        // Create the Answers, which fails.

        restAnswersMockMvc.perform(post("/api/answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answers)))
            .andExpect(status().isBadRequest());

        List<Answers> answersList = answersRepository.findAll();
        assertThat(answersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = answersRepository.findAll().size();
        // set the field null
        answers.setDateCreated(null);

        // Create the Answers, which fails.

        restAnswersMockMvc.perform(post("/api/answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answers)))
            .andExpect(status().isBadRequest());

        List<Answers> answersList = answersRepository.findAll();
        assertThat(answersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateUpdatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = answersRepository.findAll().size();
        // set the field null
        answers.setDateUpdated(null);

        // Create the Answers, which fails.

        restAnswersMockMvc.perform(post("/api/answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answers)))
            .andExpect(status().isBadRequest());

        List<Answers> answersList = answersRepository.findAll();
        assertThat(answersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsAnonymousIsRequired() throws Exception {
        int databaseSizeBeforeTest = answersRepository.findAll().size();
        // set the field null
        answers.setIsAnonymous(null);

        // Create the Answers, which fails.

        restAnswersMockMvc.perform(post("/api/answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answers)))
            .andExpect(status().isBadRequest());

        List<Answers> answersList = answersRepository.findAll();
        assertThat(answersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAnswers() throws Exception {
        // Initialize the database
        answersRepository.saveAndFlush(answers);

        // Get all the answersList
        restAnswersMockMvc.perform(get("/api/answers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(answers.getId().intValue())))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].isAnonymous").value(hasItem(DEFAULT_IS_ANONYMOUS.booleanValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getAnswers() throws Exception {
        // Initialize the database
        answersRepository.saveAndFlush(answers);

        // Get the answers
        restAnswersMockMvc.perform(get("/api/answers/{id}", answers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(answers.getId().intValue()))
            .andExpect(jsonPath("$.answer").value(DEFAULT_ANSWER.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateUpdated").value(DEFAULT_DATE_UPDATED.toString()))
            .andExpect(jsonPath("$.isAnonymous").value(DEFAULT_IS_ANONYMOUS.booleanValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAnswers() throws Exception {
        // Get the answers
        restAnswersMockMvc.perform(get("/api/answers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnswers() throws Exception {
        // Initialize the database
        answersService.save(answers);

        int databaseSizeBeforeUpdate = answersRepository.findAll().size();

        // Update the answers
        Answers updatedAnswers = answersRepository.findOne(answers.getId());
        // Disconnect from session so that the updates on updatedAnswers are not directly saved in db
        em.detach(updatedAnswers);
        updatedAnswers
            .answer(UPDATED_ANSWER)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED)
            .isAnonymous(UPDATED_IS_ANONYMOUS)
            .active(UPDATED_ACTIVE);

        restAnswersMockMvc.perform(put("/api/answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnswers)))
            .andExpect(status().isOk());

        // Validate the Answers in the database
        List<Answers> answersList = answersRepository.findAll();
        assertThat(answersList).hasSize(databaseSizeBeforeUpdate);
        Answers testAnswers = answersList.get(answersList.size() - 1);
        assertThat(testAnswers.getAnswer()).isEqualTo(UPDATED_ANSWER);
        assertThat(testAnswers.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testAnswers.getDateUpdated()).isEqualTo(UPDATED_DATE_UPDATED);
        assertThat(testAnswers.isIsAnonymous()).isEqualTo(UPDATED_IS_ANONYMOUS);
        assertThat(testAnswers.isActive()).isEqualTo(UPDATED_ACTIVE);

        // Validate the Answers in Elasticsearch
        Answers answersEs = answersSearchRepository.findOne(testAnswers.getId());
        assertThat(answersEs).isEqualToIgnoringGivenFields(testAnswers);
    }

    @Test
    @Transactional
    public void updateNonExistingAnswers() throws Exception {
        int databaseSizeBeforeUpdate = answersRepository.findAll().size();

        // Create the Answers

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAnswersMockMvc.perform(put("/api/answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answers)))
            .andExpect(status().isCreated());

        // Validate the Answers in the database
        List<Answers> answersList = answersRepository.findAll();
        assertThat(answersList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAnswers() throws Exception {
        // Initialize the database
        answersService.save(answers);

        int databaseSizeBeforeDelete = answersRepository.findAll().size();

        // Get the answers
        restAnswersMockMvc.perform(delete("/api/answers/{id}", answers.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean answersExistsInEs = answersSearchRepository.exists(answers.getId());
        assertThat(answersExistsInEs).isFalse();

        // Validate the database is empty
        List<Answers> answersList = answersRepository.findAll();
        assertThat(answersList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAnswers() throws Exception {
        // Initialize the database
        answersService.save(answers);

        // Search the answers
        restAnswersMockMvc.perform(get("/api/_search/answers?query=id:" + answers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(answers.getId().intValue())))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].isAnonymous").value(hasItem(DEFAULT_IS_ANONYMOUS.booleanValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Answers.class);
        Answers answers1 = new Answers();
        answers1.setId(1L);
        Answers answers2 = new Answers();
        answers2.setId(answers1.getId());
        assertThat(answers1).isEqualTo(answers2);
        answers2.setId(2L);
        assertThat(answers1).isNotEqualTo(answers2);
        answers1.setId(null);
        assertThat(answers1).isNotEqualTo(answers2);
    }
}
