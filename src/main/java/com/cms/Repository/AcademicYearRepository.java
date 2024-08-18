package com.cms.Repository;

import com.cms.Model.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcademicYearRepository extends JpaRepository<AcademicYear, Integer> {
//    AcademicYear findById(int id);

    boolean existsById(int academicYearId);

    boolean existsByAcademicYear(String AcademicYear);
    Optional<AcademicYear> findById(int academicYearId);
}
