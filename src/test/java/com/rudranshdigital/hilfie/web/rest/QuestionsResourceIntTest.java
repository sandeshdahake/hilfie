package com.rudranshdigital.hilfie.web.rest;

import com.rudranshdigital.hilfie.HilfieApp;

import com.rudranshdigital.hilfie.domain.Questions;
import com.rudranshdigital.hilfie.domain.User;
import com.rudranshdigital.hilfie.domain.Classroom;
import com.rudranshdigital.hilfie.repository.QuestionsRepository;
import com.rudranshdigital.hilfie.service.QuestionsService;
import com.rudranshdigital.hilfie.repository.search.QuestionsSearchRepository;
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
 * Test class for the QuestionsResource REST controller.
 *
 * @see QuestionsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HilfieApp.class)
public class QuestionsResourceIntTest {

    private static final String DEFAULT_QUESTIONLABEL = "AAAAAAAAAA";
    private static final String UPDATED_QUESTIONLABEL = "BBBBBBBBBB";

    private static final String DEFAULT_QUESTION = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_UPDATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_UPDATED = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_ANONYMOUS = false;
    private static final Boolean UPDATED_IS_ANONYMOUS = true;

    private static final Integer DEFAULT_ANSWER_COUNT = 1;
    private static final Integer UPDATED_ANSWER_COUNT = 2;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private QuestionsRepository questionsRepository;

    @Autowired
    private QuestionsService questionsService;

    @Autowired
    private QuestionsSearchRepository questionsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restQuestionsMockMvc;

    private Questions questions;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final QuestionsResource questionsResource = new QuestionsResource(questionsService);
        this.restQuestionsMockMvc = MockMvcBuilders.standaloneSetup(questionsResource)
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
    public static Questions createEntity(EntityManager em) {
        Questions questions = new Questions()
            .questionlabel(DEFAULT_QUESTIONLABEL)
            .question(DEFAULT_QUESTION)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateUpdated(DEFAULT_DATE_UPDATED)
            .isAnonymous(DEFAULT_IS_ANONYMOUS)
            .answerCount(DEFAULT_ANSWER_COUNT)
            .active(DEFAULT_ACTIVE);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        questions.setUser(user);
        // Add required entity
        Classroom classroom = ClassroomResourceIntTest.createEntity(em);
        em.persist(classroom);
        em.flush();
        questions.setClassroom(classroom);
        return questions;
    }

    @Before
    public void initTest() {
        questionsSearchRepository.deleteAll();
        questions = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuestions() throws Exception {
        int databaseSizeBeforeCreate = questionsRepository.findAll().size();

        // Create the Questions
        restQuestionsMockMvc.perform(post("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questions)))
            .andExpect(status().isCreated());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeCreate + 1);
        Questions testQuestions = questionsList.get(questionsList.size() - 1);
        assertThat(testQuestions.getQuestionlabel()).isEqualTo(DEFAULT_QUESTIONLABEL);
        assertThat(testQuestions.getQuestion()).isEqualTo(DEFAULT_QUESTION);
        assertThat(testQuestions.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testQuestions.getDateUpdated()).isEqualTo(DEFAULT_DATE_UPDATED);
        assertThat(testQuestions.isIsAnonymous()).isEqualTo(DEFAULT_IS_ANONYMOUS);
        assertThat(testQuestions.getAnswerCount()).isEqualTo(DEFAULT_ANSWER_COUNT);
        assertThat(testQuestions.isActive()).isEqualTo(DEFAULT_ACTIVE);

        // Validate the Questions in Elasticsearch
        Questions questionsEs = questionsSearchRepository.findOne(testQuestions.getId());
        assertThat(questionsEs).isEqualToIgnoringGivenFields(testQuestions);
    }

    @Test
    @Transactional
    public void createQuestionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = questionsRepository.findAll().size();

        // Create the Questions with an existing ID
        questions.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionsMockMvc.perform(post("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questions)))
            .andExpect(status().isBadRequest());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkQuestionlabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionsRepository.findAll().size();
        // set the field null
        questions.setQuestionlabel(null);

        // Create the Questions, which fails.

        restQuestionsMockMvc.perform(post("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questions)))
            .andExpect(status().isBadRequest());

        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionsRepository.findAll().size();
        // set the field null
        questions.setDateCreated(null);

        // Create the Questions, which fails.

        restQuestionsMockMvc.perform(post("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questions)))
            .andExpect(status().isBadRequest());

        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateUpdatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionsRepository.findAll().size();
        // set the field null
        questions.setDateUpdated(null);

        // Create the Questions, which fails.

        restQuestionsMockMvc.perform(post("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questions)))
            .andExpect(status().isBadRequest());

        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionsRepository.findAll().size();
        // set the field null
        questions.setActive(null);

        // Create the Questions, which fails.

        restQuestionsMockMvc.perform(post("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questions)))
            .andExpect(status().isBadRequest());

        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQuestions() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList
        restQuestionsMockMvc.perform(get("/api/questions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questions.getId().intValue())))
            .andExpect(jsonPath("$.[*].questionlabel").value(hasItem(DEFAULT_QUESTIONLABEL.toString())))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].isAnonymous").value(hasItem(DEFAULT_IS_ANONYMOUS.booleanValue())))
            .andExpect(jsonPath("$.[*].answerCount").value(hasItem(DEFAULT_ANSWER_COUNT)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getQuestions() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get the questions
        restQuestionsMockMvc.perform(get("/api/questions/{id}", questions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(questions.getId().intValue()))
            .andExpect(jsonPath("$.questionlabel").value(DEFAULT_QUESTIONLABEL.toString()))
            .andExpect(jsonPath("$.question").value(DEFAULT_QUESTION.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateUpdated").value(DEFAULT_DATE_UPDATED.toString()))
            .andExpect(jsonPath("$.isAnonymous").value(DEFAULT_IS_ANONYMOUS.booleanValue()))
            .andExpect(jsonPath("$.answerCount").value(DEFAULT_ANSWER_COUNT))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingQuestions() throws Exception {
        // Get the questions
        restQuestionsMockMvc.perform(get("/api/questions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestions() throws Exception {
        // Initialize the database
        questionsService.save(questions);

        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();

        // Update the questions
        Questions updatedQuestions = questionsRepository.findOne(questions.getId());
        // Disconnect from session so that the updates on updatedQuestions are not directly saved in db
        em.detach(updatedQuestions);
        updatedQuestions
            .questionlabel(UPDATED_QUESTIONLABEL)
            .question(UPDATED_QUESTION)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED)
            .isAnonymous(UPDATED_IS_ANONYMOUS)
            .answerCount(UPDATED_ANSWER_COUNT)
            .active(UPDATED_ACTIVE);

        restQuestionsMockMvc.perform(put("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedQuestions)))
            .andExpect(status().isOk());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
        Questions testQuestions = questionsList.get(questionsList.size() - 1);
        assertThat(testQuestions.getQuestionlabel()).isEqualTo(UPDATED_QUESTIONLABEL);
        assertThat(testQuestions.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testQuestions.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testQuestions.getDateUpdated()).isEqualTo(UPDATED_DATE_UPDATED);
        assertThat(testQuestions.isIsAnonymous()).isEqualTo(UPDATED_IS_ANONYMOUS);
        assertThat(testQuestions.getAnswerCount()).isEqualTo(UPDATED_ANSWER_COUNT);
        assertThat(testQuestions.isActive()).isEqualTo(UPDATED_ACTIVE);

        // Validate the Questions in Elasticsearch
        Questions questionsEs = questionsSearchRepository.findOne(testQuestions.getId());
        assertThat(questionsEs).isEqualToIgnoringGivenFields(testQuestions);
    }

    @Test
    @Transactional
    public void updateNonExistingQuestions() throws Exception {
        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();

        // Create the Questions

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restQuestionsMockMvc.perform(put("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questions)))
            .andExpect(status().isCreated());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteQuestions() throws Exception {
        // Initialize the database
        questionsService.save(questions);

        int databaseSizeBeforeDelete = questionsRepository.findAll().size();

        // Get the questions
        restQuestionsMockMvc.perform(delete("/api/questions/{id}", questions.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean questionsExistsInEs = questionsSearchRepository.exists(questions.getId());
        assertThat(questionsExistsInEs).isFalse();

        // Validate the database is empty
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchQuestions() throws Exception {
        // Initialize the database
        questionsService.save(questions);

        // Search the questions
        restQuestionsMockMvc.perform(get("/api/_search/questions?query=id:" + questions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questions.getId().intValue())))
            .andExpect(jsonPath("$.[*].questionlabel").value(hasItem(DEFAULT_QUESTIONLABEL.toString())))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].isAnonymous").value(hasItem(DEFAULT_IS_ANONYMOUS.booleanValue())))
            .andExpect(jsonPath("$.[*].answerCount").value(hasItem(DEFAULT_ANSWER_COUNT)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Questions.class);
        Questions questions1 = new Questions();
        questions1.setId(1L);
        Questions questions2 = new Questions();
        questions2.setId(questions1.getId());
        assertThat(questions1).isEqualTo(questions2);
        questions2.setId(2L);
        assertThat(questions1).isNotEqualTo(questions2);
        questions1.setId(null);
        assertThat(questions1).isNotEqualTo(questions2);
    }
}
