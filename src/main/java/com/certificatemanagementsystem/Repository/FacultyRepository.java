package com.certificatemanagementsystem.Repository;

import com.certificatemanagementsystem.Model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Integer> {
    Faculty findById(int facultyId);

    Faculty findByFacultyName(String facultyName);

    Optional<Faculty> getFacultyById(int facultyId);

    //    List<Faculty> findByIdOrName(int facultyId, String facultyName);
}
