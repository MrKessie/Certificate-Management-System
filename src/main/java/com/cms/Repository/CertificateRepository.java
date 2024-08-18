package com.cms.Repository;

import com.cms.Model.Certificate;
import com.cms.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Integer> {
    Optional<Certificate> findByStudentId(Student studentId);
    List<Certificate> findAllByStudentId(Student studentId);



//    Optional<Certificate> findByStudentId(Student student);
}
