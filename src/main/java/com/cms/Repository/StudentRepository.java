package com.cms.Repository;

import com.cms.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    boolean existsByStudentId(int studentId);

    boolean existsByStudentName(String studentName);
    Student findByStudentId(int student);

}
