package com.rudranshdigital.hilfie.repository;

import com.rudranshdigital.hilfie.domain.School;
import com.rudranshdigital.hilfie.domain.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the UserProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    UserProfile findByUserLogin(String login);

    Page<UserProfile> findAllBySchool(Pageable pageable, School school);
}
