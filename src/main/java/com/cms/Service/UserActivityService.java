package com.cms.Service;

import com.cms.Model.User;
import com.cms.Model.UserActivity;
import com.cms.Repository.UserActivityRepository;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserActivityService {

    @Autowired
    private UserActivityRepository userActivityRepository;


    @Transactional
    public void logActivity(User user, String action, String details) {
        UserActivity activity = new UserActivity();
        activity.setUser(user);
        activity.setAction(action);
        activity.setDetails(details);
        activity.setTimestamp(LocalDateTime.now());

        userActivityRepository.save(activity);
    }

    public List<UserActivity> userActivityList() {
        return userActivityRepository.findAll();
    }


    public List<UserActivity> getActivitiesWithinDateRange(LocalDate fromDate, LocalDate toDate) {
        return userActivityRepository.findByTimestampBetween(fromDate.atStartOfDay(), toDate.atTime(23, 59, 59));
    }
}
