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
 * A School.
 */
@Entity
@Table(name = "school")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "school")
public class School implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "school_name", nullable = false)
    private String schoolName;

    @NotNull
    @Lob
    @Column(name = "school_address", nullable = false)
    private String schoolAddress;

    @NotNull
    @Column(name = "school_phone", nullable = false)
    private String schoolPhone;

    @NotNull
    @Column(name = "school_fax", nullable = false)
    private String schoolFax;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Column(name = "jhi_activate", nullable = false)
    private Boolean activate;

    @OneToMany(mappedBy = "schoolName")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Classroom> classNames = new HashSet<>();

    @OneToMany(mappedBy = "school")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UserProfile> ids = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public School schoolName(String schoolName) {
        this.schoolName = schoolName;
        return this;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolAddress() {
        return schoolAddress;
    }

    public School schoolAddress(String schoolAddress) {
        this.schoolAddress = schoolAddress;
        return this;
    }

    public void setSchoolAddress(String schoolAddress) {
        this.schoolAddress = schoolAddress;
    }

    public String getSchoolPhone() {
        return schoolPhone;
    }

    public School schoolPhone(String schoolPhone) {
        this.schoolPhone = schoolPhone;
        return this;
    }

    public void setSchoolPhone(String schoolPhone) {
        this.schoolPhone = schoolPhone;
    }

    public String getSchoolFax() {
        return schoolFax;
    }

    public School schoolFax(String schoolFax) {
        this.schoolFax = schoolFax;
        return this;
    }

    public void setSchoolFax(String schoolFax) {
        this.schoolFax = schoolFax;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public School startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public School endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean isActivate() {
        return activate;
    }

    public School activate(Boolean activate) {
        this.activate = activate;
        return this;
    }

    public void setActivate(Boolean activate) {
        this.activate = activate;
    }

    public Set<Classroom> getClassNames() {
        return classNames;
    }

    public School classNames(Set<Classroom> classrooms) {
        this.classNames = classrooms;
        return this;
    }

    public School addClassName(Classroom classroom) {
        this.classNames.add(classroom);
        classroom.setSchoolName(this);
        return this;
    }

    public School removeClassName(Classroom classroom) {
        this.classNames.remove(classroom);
        classroom.setSchoolName(null);
        return this;
    }

    public void setClassNames(Set<Classroom> classrooms) {
        this.classNames = classrooms;
    }

    public Set<UserProfile> getIds() {
        return ids;
    }

    public School ids(Set<UserProfile> userProfiles) {
        this.ids = userProfiles;
        return this;
    }

    public School addId(UserProfile userProfile) {
        this.ids.add(userProfile);
        userProfile.setSchool(this);
        return this;
    }

    public School removeId(UserProfile userProfile) {
        this.ids.remove(userProfile);
        userProfile.setSchool(null);
        return this;
    }

    public void setIds(Set<UserProfile> userProfiles) {
        this.ids = userProfiles;
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
        School school = (School) o;
        if (school.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), school.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "School{" +
            "id=" + getId() +
            ", schoolName='" + getSchoolName() + "'" +
            ", schoolAddress='" + getSchoolAddress() + "'" +
            ", schoolPhone='" + getSchoolPhone() + "'" +
            ", schoolFax='" + getSchoolFax() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", activate='" + isActivate() + "'" +
            "}";
    }
}
