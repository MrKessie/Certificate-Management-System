package com.cms.Controller;

import com.cms.Model.AcademicYear;
import com.cms.Model.User;
import com.cms.Model.UserActivity;
import com.cms.Service.UserActivityService;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/user-activity")
public class UserActivityController {

    @Autowired
    UserActivityService userActivityService;

    @GetMapping
    public String showAddAcademicYearPage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("user-activities", userActivityService.userActivityList());
        return "user-activity";
    }

    @GetMapping("/all")
    @ResponseBody
    public List<UserActivity> userActivityList() {
        return userActivityService.userActivityList();
    }


    @GetMapping("/export-user-activities")
    public ResponseEntity<byte[]> exportUserActivitiesToPDF(
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) throws IOException {

        // Fetch activities from the database within the date range
        List<UserActivity> activities = userActivityService.getActivitiesWithinDateRange(fromDate, toDate);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Add Title
        document.add(new Paragraph("CERTIFICATE MANAGEMENT SYSTEM")
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFontSize(25));

        document.add(new Paragraph("User Activities Report")
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFontSize(16));
        document.add(new Paragraph("From: " + fromDate + " To: " + toDate).setTextAlignment(TextAlignment.CENTER));

        // Create table with 5 columns
        float[] columnWidths = {1, 1, 2, 3, 2};
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        // Add table headers
        table.addHeaderCell(new Cell().add(new Paragraph("Activity ID").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("User ID").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Action").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Details").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Date/Time").setBold()));

        // Populate table with user activity data
        for (UserActivity activity : activities) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(activity.getId()))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(activity.getUser())))); // Assuming `getUserId()` returns user ID
            table.addCell(new Cell().add(new Paragraph(activity.getAction())));
            table.addCell(new Cell().add(new Paragraph(activity.getDetails())));
            table.addCell(new Cell().add(new Paragraph(activity.getTimestamp().toString())));
        }

        // Add the table to the document
        document.add(table);
        document.close();

        // Prepare the PDF response
        byte[] pdfBytes = out.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=User_Activities_Report.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
