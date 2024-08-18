package com.cms.Service;

import com.cms.Model.CertificateVerify;
import com.cms.Model.Student;
import com.cms.Model.User;
import com.cms.Repository.CertificateVerifyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CertificateVerifyService {

    @Autowired
    CertificateVerifyRepository certificateVerifyRepository;

    public void saveVerification(User user, Student student, boolean verified) {
        CertificateVerify verification = new CertificateVerify();
        verification.setUser(user);
        verification.setStudent(student);
//        verification.setEmployer(employer);
//        verification.setOrganization(organization);
        verification.setVerified(verified);
        verification.setDateVerified(LocalDateTime.now());
        certificateVerifyRepository.save(verification);
    }
}
