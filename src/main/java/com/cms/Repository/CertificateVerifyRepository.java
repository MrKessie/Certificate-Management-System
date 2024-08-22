package com.cms.Repository;

import com.cms.Model.Certificate;
import com.cms.Model.CertificateVerify;
import com.cms.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CertificateVerifyRepository extends JpaRepository<CertificateVerify, Integer> {

    Optional<CertificateVerify> findByStudent(Student studentId);

    boolean existsByVerificationId(int verificationId);
}
