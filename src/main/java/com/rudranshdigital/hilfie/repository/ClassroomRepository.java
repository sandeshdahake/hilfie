package com.rudranshdigital.hilfie.repository;

import com.rudranshdigital.hilfie.domain.Classroom;
import com.rudranshdigital.hilfie.domain.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Classroom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {

    Page<Classroom> findAllBySchoolName(Pageable pageable, School school);
}
