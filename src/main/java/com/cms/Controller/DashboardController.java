package com.cms.Controller;

import com.cms.Model.User;
import com.cms.Service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final AcademicYearService academicYearService;
    private final FacultyService facultyService;
    private final DepartmentService departmentService;
    private final StudentService studentService;
    private final CertificateService certificateService;
    private final ProgrammeService programmeService;
    private final CertificateVerifyService certificateVerifyService;
    private final CertificateIssueService certificateIssueService;
    private final UserService userService;

    public DashboardController(AcademicYearService academicYearService, FacultyService facultyService, DepartmentService departmentService,
                               StudentService studentService, CertificateService certificateService, ProgrammeService programmeService,
                               CertificateVerifyService certificateVerifyService, CertificateIssueService certificateIssueService, UserService userService) {
        this.academicYearService = academicYearService;
        this.facultyService = facultyService;
        this.departmentService = departmentService;
        this.studentService = studentService;
        this.certificateService = certificateService;
        this.programmeService = programmeService;
        this.certificateVerifyService = certificateVerifyService;
        this.certificateIssueService = certificateIssueService;
        this.userService = userService;
    }

    @GetMapping("/admin/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);

        model.addAttribute("totalAcademicYears", academicYearService.totalAcademicYears());
        model.addAttribute("totalFaculties", facultyService.totalFaculties());
        model.addAttribute("totalDepartments", departmentService.totalDepartments());
        model.addAttribute("totalProgrammes", programmeService.totalProgrammes());
        model.addAttribute("totalStudents", studentService.totalStudents());
        model.addAttribute("totalUsers", userService.totalUsers());
        model.addAttribute("totalVerifiedCertificates", certificateVerifyService.totalVerifiedCertificates());
        model.addAttribute("totalIssuedCertificates", certificateIssueService.totalCertificatesIssued());
        model.addAttribute("totalCertificates", certificateService.totalCertificates());

        return "admin-dashboard";
    }

    @GetMapping("/client/dashboard")
    public String showDashboardPage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);

        model.addAttribute("totalAcademicYears", academicYearService.totalAcademicYears());
        model.addAttribute("totalFaculties", facultyService.totalFaculties());
        model.addAttribute("totalDepartments", departmentService.totalDepartments());
        model.addAttribute("totalProgrammes", programmeService.totalProgrammes());
        model.addAttribute("totalStudents", studentService.totalStudents());
        model.addAttribute("totalUsers", userService.totalUsers());
        model.addAttribute("totalVerifiedCertificates", certificateVerifyService.totalVerifiedCertificates());
        model.addAttribute("totalIssuedCertificates", certificateIssueService.totalCertificatesIssued());
        model.addAttribute("totalCertificates", certificateService.totalCertificates());

        return "client-dashboard";
    }

    @GetMapping("/dashboard/homepage")
    public String showHomePage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);

        model.addAttribute("totalAcademicYears", academicYearService.totalAcademicYears());
        model.addAttribute("totalFaculties", facultyService.totalFaculties());
        model.addAttribute("totalDepartments", departmentService.totalDepartments());
        model.addAttribute("totalProgrammes", programmeService.totalProgrammes());
        model.addAttribute("totalStudents", studentService.totalStudents());
        model.addAttribute("totalUsers", userService.totalUsers());
        model.addAttribute("totalVerifiedCertificates", certificateVerifyService.totalVerifiedCertificates());
        model.addAttribute("totalIssuedCertificates", certificateIssueService.totalCertificatesIssued());
        model.addAttribute("totalCertificates", certificateService.totalCertificates());


        return "admin-dashboard";
    }
}
