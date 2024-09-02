//package com.cms.Controller;
//
//import com.cms.Enum.Roles;
//import com.cms.Model.User;
//import com.cms.Model.UserActivity;
//import com.cms.Repository.UserRepository;
//import com.cms.Service.DatabaseStatsService;
//import com.cms.Service.PdfReportService;
//import com.cms.Service.UserActivityService;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.annotation.Secured;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.io.IOException;
//import java.security.Principal;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//@Controller
//@RequestMapping("/reports")
//public class ReportController {
//
//    @Autowired
//    private PdfReportService pdfReportService;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    private UserActivityService userActivityService;
//
//    @Autowired
//    private DatabaseStatsService databaseStatsService;
//
//    // ... other endpoints
//
////    @GetMapping("/user-activity/pdf")
////    public ResponseEntity<byte[]> getUserActivityPdf(@RequestParam int userId) throws IOException {
////        User user = userRepository.findById(userId);
////
////        // Check if the user is an admin
////        if (!user.getRole().equals(Roles.ROLE_ADMIN)) {
////            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
////        }
////
////
////        List<UserActivity> activities = userActivityService.getUserActivities(user);
////        byte[] pdfBytes = pdfReportService.generateUserActivityReport(activities);
////
////        HttpHeaders headers = new HttpHeaders();
////        headers.setContentType(MediaType.APPLICATION_PDF);
////        headers.add("Content-Disposition", "inline; filename=user_activity_report.pdf");
//////        headers.setContentDispositionFormData("filename", "user_activity_report.pdf");
////
////
////        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
////    }
//
//
//    @GetMapping("/user-activity/pdf")
//    public ResponseEntity<byte[]> getUserActivityPdf(Principal principal) throws IOException {
//        Optional<Optional<User>> user = Optional.ofNullable(userRepository.findByUsername(principal.getName()));
////        if (user.get() != Roles.ROLE_ADMIN) {
////            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
////        }
//
//        List<UserActivity> activities = userActivityService.getAllUserActivities();
//        byte[] pdfBytes = pdfReportService.generateUserActivityReport(activities);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        headers.setContentDispositionFormData("filename", "user_activity_report.pdf");
//
//        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
//    }
//
//
//    @GetMapping("/table-stats/pdf")
//    public ResponseEntity<byte[]> getTableStatsPdf() throws IOException {
//        Map<String, Long> tableStats = databaseStatsService.getTableRowCounts();
//        byte[] pdfBytes = pdfReportService.generateTableStatsReport(tableStats);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        headers.add("Content-Disposition", "inline; filename=table_stats_report.pdf");
////        headers.setContentDispositionFormData("filename", "table_stats_report.pdf");
//
//        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
//    }
//}
