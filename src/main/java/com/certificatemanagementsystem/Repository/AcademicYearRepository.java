package com.certificatemanagementsystem.Repository;

import com.certificatemanagementsystem.Model.AcademicYear;
import com.certificatemanagementsystem.Model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcademicYearRepository extends JpaRepository<AcademicYear, Integer> {

    AcademicYear findById(int yearId);

    Optional<AcademicYear> getAcademicYearByYearId(int yearId);

    @Query("SELECT a FROM AcademicYear a ORDER BY a.yearId ASC")
    List<AcademicYear> findAllByIdAsc();

    @Query("SELECT a FROM AcademicYear a ORDER BY a.yearId DESC")
    List<AcademicYear> findAllByIdDesc();

    @Query("SELECT a FROM AcademicYear a ORDER BY a.year ASC")
    List<AcademicYear> findAllByYearAsc();

    @Query("SELECT a FROM AcademicYear a ORDER BY a.year DESC")
    List<AcademicYear> findAllByYearDesc();

    @Query("SELECT a FROM AcademicYear a ORDER BY a.dateAdded ASC")
    List<AcademicYear> findAllByDateAddedAsc();

    @Query("SELECT a FROM AcademicYear a ORDER BY a.dateAdded DESC")
    List<AcademicYear> findAllByDateAddedDesc();



}
