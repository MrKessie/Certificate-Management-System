package com.certificatemanagementsystem.Repository;

import com.certificatemanagementsystem.Model.Programme;
import com.certificatemanagementsystem.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Student findById(int studentId);

    Optional<Student> getStudentById(int studentId);
}
