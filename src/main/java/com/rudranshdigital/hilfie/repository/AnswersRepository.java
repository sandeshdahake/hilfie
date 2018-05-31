package com.rudranshdigital.hilfie.repository;

import com.rudranshdigital.hilfie.domain.Answers;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Answers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnswersRepository extends JpaRepository<Answers, Long> {

    @Query("select answers from Answers answers where answers.user.login = ?#{principal.username}")
    List<Answers> findByUserIsCurrentUser();

}
