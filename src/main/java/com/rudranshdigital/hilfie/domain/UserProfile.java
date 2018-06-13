package com.rudranshdigital.hilfie.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A UserProfile.
 */
@Entity
@Table(name = "user_profile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "userprofile")
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_phone")
    private String userPhone;

    @Column(name = "user_dob")
    private LocalDate userDob;

    @Column(name = "user_blood_group")
    private String userBloodGroup;

    @Column(name = "user_image")
    private String userImage;

    @NotNull
    @Column(name = "jhi_activate", nullable = false)
    private Boolean activate;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    private School school;

    @ManyToOne
    private Classroom classroom;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public UserProfile userPhone(String userPhone) {
        this.userPhone = userPhone;
        return this;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public LocalDate getUserDob() {
        return userDob;
    }

    public UserProfile userDob(LocalDate userDob) {
        this.userDob = userDob;
        return this;
    }

    public void setUserDob(LocalDate userDob) {
        this.userDob = userDob;
    }

    public String getUserBloodGroup() {
        return userBloodGroup;
    }

    public UserProfile userBloodGroup(String userBloodGroup) {
        this.userBloodGroup = userBloodGroup;
        return this;
    }

    public void setUserBloodGroup(String userBloodGroup) {
        this.userBloodGroup = userBloodGroup;
    }

    public String getUserImage() {
        return userImage;
    }

    public UserProfile userImage(String userImage) {
        this.userImage = userImage;
        return this;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public Boolean isActivate() {
        return activate;
    }

    public UserProfile activate(Boolean activate) {
        this.activate = activate;
        return this;
    }

    public void setActivate(Boolean activate) {
        this.activate = activate;
    }

    public User getUser() {
        return user;
    }

    public UserProfile user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public School getSchool() {
        return school;
    }

    public UserProfile school(School school) {
        this.school = school;
        return this;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public UserProfile classroom(Classroom classroom) {
        this.classroom = classroom;
        return this;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
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
        UserProfile userProfile = (UserProfile) o;
        if (userProfile.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userProfile.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserProfile{" +
            "id=" + getId() +
            ", userPhone='" + getUserPhone() + "'" +
            ", userDob='" + getUserDob() + "'" +
            ", userBloodGroup='" + getUserBloodGroup() + "'" +
            ", userImage='" + getUserImage() + "'" +
            ", activate='" + isActivate() + "'" +
            "}";
    }
}
