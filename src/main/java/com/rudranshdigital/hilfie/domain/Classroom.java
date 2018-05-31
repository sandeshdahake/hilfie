package com.rudranshdigital.hilfie.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Classroom.
 */
@Entity
@Table(name = "classroom")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "classroom")
public class Classroom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "class_name", nullable = false)
    private String className;

    @Column(name = "description")
    private String description;

    @ManyToOne(optional = false)
    @NotNull
    private School schoolName;

    @OneToMany(mappedBy = "classroom")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Questions> questions = new HashSet<>();

    @OneToMany(mappedBy = "classroom")
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

    public String getClassName() {
        return className;
    }

    public Classroom className(String className) {
        this.className = className;
        return this;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDescription() {
        return description;
    }

    public Classroom description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public School getSchoolName() {
        return schoolName;
    }

    public Classroom schoolName(School school) {
        this.schoolName = school;
        return this;
    }

    public void setSchoolName(School school) {
        this.schoolName = school;
    }

    public Set<Questions> getQuestions() {
        return questions;
    }

    public Classroom questions(Set<Questions> questions) {
        this.questions = questions;
        return this;
    }

    public Classroom addQuestion(Questions questions) {
        this.questions.add(questions);
        questions.setClassroom(this);
        return this;
    }

    public Classroom removeQuestion(Questions questions) {
        this.questions.remove(questions);
        questions.setClassroom(null);
        return this;
    }

    public void setQuestions(Set<Questions> questions) {
        this.questions = questions;
    }

    public Set<Answers> getAnswers() {
        return answers;
    }

    public Classroom answers(Set<Answers> answers) {
        this.answers = answers;
        return this;
    }

    public Classroom addAnswer(Answers answers) {
        this.answers.add(answers);
        answers.setClassroom(this);
        return this;
    }

    public Classroom removeAnswer(Answers answers) {
        this.answers.remove(answers);
        answers.setClassroom(null);
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
        Classroom classroom = (Classroom) o;
        if (classroom.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), classroom.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Classroom{" +
            "id=" + getId() +
            ", className='" + getClassName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
