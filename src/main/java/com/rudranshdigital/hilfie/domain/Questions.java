package com.rudranshdigital.hilfie.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Questions.
 */
@Entity
@Table(name = "questions")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "questions")
public class Questions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "questionlabel", nullable = false)
    private String questionlabel;

    @Lob
    @Column(name = "question")
    private String question;

    @NotNull
    @Column(name = "date_created", nullable = false)
    private LocalDate dateCreated;

    @NotNull
    @Column(name = "date_updated", nullable = false)
    private LocalDate dateUpdated;

    @Column(name = "is_anonymous")
    private Boolean isAnonymous;

    @Column(name = "answer_count")
    private Integer answerCount;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    private Classroom classroom;

    @OneToMany(mappedBy = "questions")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Answers> answers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionlabel() {
        return questionlabel;
    }

    public Questions questionlabel(String questionlabel) {
        this.questionlabel = questionlabel;
        return this;
    }

    public void setQuestionlabel(String questionlabel) {
        this.questionlabel = questionlabel;
    }

    public String getQuestion() {
        return question;
    }

    public Questions question(String question) {
        this.question = question;
        return this;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public Questions dateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDate getDateUpdated() {
        return dateUpdated;
    }

    public Questions dateUpdated(LocalDate dateUpdated) {
        this.dateUpdated = dateUpdated;
        return this;
    }

    public void setDateUpdated(LocalDate dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Boolean isIsAnonymous() {
        return isAnonymous;
    }

    public Questions isAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
        return this;
    }

    public void setIsAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public Integer getAnswerCount() {
        return answerCount;
    }

    public Questions answerCount(Integer answerCount) {
        this.answerCount = answerCount;
        return this;
    }

    public void setAnswerCount(Integer answerCount) {
        this.answerCount = answerCount;
    }

    public Boolean isActive() {
        return active;
    }

    public Questions active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public User getUser() {
        return user;
    }

    public Questions user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
            this.user = user;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public Questions classroom(Classroom classroom) {
        this.classroom = classroom;
        return this;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public Set<Answers> getAnswers() {
        return answers;
    }

    public Questions answers(Set<Answers> answers) {
        this.answers = answers;
        return this;
    }

    public Questions addAnswer(Answers answers) {
        this.answers.add(answers);
        answers.setQuestions(this);
        return this;
    }

    public Questions removeAnswer(Answers answers) {
        this.answers.remove(answers);
        answers.setQuestions(null);
        return this;
    }

    public void setAnswers(Set<Answers> answers) {
        this.answers = answers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Questions questions = (Questions) o;
        if (questions.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), questions.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Questions{" +
            "id=" + getId() +
            ", questionlabel='" + getQuestionlabel() + "'" +
            ", question='" + getQuestion() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateUpdated='" + getDateUpdated() + "'" +
            ", isAnonymous='" + isIsAnonymous() + "'" +
            ", answerCount=" + getAnswerCount() +
            ", active='" + isActive() + "'" +
            "}";
    }
}
