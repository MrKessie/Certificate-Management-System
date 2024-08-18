package com.cms.Repository;

import com.cms.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
//    Optional<Student> findByStudentId(int studentId);

//    Student findById(int studentId);
    Student findByStudentId(int student);
}
