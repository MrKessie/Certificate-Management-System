package com.certificatemanagementsystem.Repository;

import com.certificatemanagementsystem.Model.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicYearRepository extends JpaRepository<AcademicYear, Integer> {

    AcademicYear findById(int yearId);

}
