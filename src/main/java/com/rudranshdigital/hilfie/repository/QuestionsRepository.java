package com.rudranshdigital.hilfie.repository;

import com.rudranshdigital.hilfie.domain.Questions;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Questions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionsRepository extends JpaRepository<Questions, Long> {

    @Query("select questions from Questions questions where questions.user.login = ?#{principal.username}")
    List<Questions> findByUserIsCurrentUser();

}
