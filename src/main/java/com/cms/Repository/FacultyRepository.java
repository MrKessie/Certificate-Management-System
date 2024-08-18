package com.cms.Repository;

import com.cms.Model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Integer> {

    Faculty findByFacultyId(int facultyId);
    boolean existsByFacultyId(int facultyId);

    Optional<Faculty> getByFacultyId(int facultyId);

    Optional<Faculty> findByFacultyIdOrFacultyName(int facultyId, String facultyName);

}
