package com.cms.Controller;

import com.cms.ExtractedDatas;
import com.cms.PDFExtractors;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/api")  // replace with your desired URL path for the endpoint.
// This controller is responsible for processing the uploaded PDF file and extracting relevant data.
public class test3Controller {

    @GetMapping
    public String showForm() {
        return "test3";
    }

    @PostMapping("/upload")
    public ResponseEntity<ExtractedDatas> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String pdfText = PDFExtractors.extractTextFromPDF(file.getInputStream());
        ExtractedDatas extractedData = extractData(pdfText);
        return ResponseEntity.ok(extractedData);
    }

    private ExtractedDatas extractData(String pdfText) {
        String name = extractName(pdfText);
        String programme = extractProgramme(pdfText);
        String course = extractCourse(pdfText);
        String classHonours = extractClass(pdfText);

        return new ExtractedDatas(name, programme, course, classHonours);
    }

    private String extractName(String text) {
        Pattern pattern = Pattern.compile("certify that\\s+([A-Z\\s]+)\\s+having pursued");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1).trim() : "";
    }

    private String extractProgramme(String text) {
        Pattern pattern = Pattern.compile("to the degree of\\s+(Bachelor of [A-Za-z\\s]+)\\s+with");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1).trim() : "";
    }

    private String extractCourse(String text) {
        Pattern pattern = Pattern.compile("in\\s+([A-Za-z\\s]+Education)");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1).trim() : "";
    }

    private String extractClass(String text) {
        Pattern pattern = Pattern.compile("with\\s+(\\w+\\s+CLASS\\s+HONOURS\\s+\\([^)]+\\))");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1).trim() : "";
    }
}
