package com.cms.Repository;

import com.cms.Model.CertificateIssue;
import com.cms.Model.CertificateVerify;
import com.cms.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CertificateIssueRepository extends JpaRepository<CertificateIssue, Integer> {

    Optional<CertificateIssue> findByStudentId(Student studentId);
}
