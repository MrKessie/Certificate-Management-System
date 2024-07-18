package com.certificatemanagementsystem.Repository;

import com.certificatemanagementsystem.Model.AcademicYear;
import com.certificatemanagementsystem.Model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcademicYearRepository extends JpaRepository<AcademicYear, Integer> {

    AcademicYear findById(int yearId);

    Optional<AcademicYear> getAcademicYearByYearId(int yearId);

}
