package com.cms.Controller;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller
public class test2Controller {

    @GetMapping("/pdf-test")  // Use this method to display the form in the view
    public String showForm() {
        return "test2";
    }

//    @PostMapping("/upload-pdf")
//    public ResponseEntity<Map<String, String>> uploadPDF(@RequestParam("file") MultipartFile file) {
//        Map<String, String> extractedData = new HashMap<>();
//        try (PDDocument document = PDDocument.load(file.getInputStream())) {  // Ensure load method is recognized
//            PDFTextStripper pdfStripper = new PDFTextStripper();
//            String text = pdfStripper.getText(document);
//
//            // Extract details
//            extractedData.put("name", extractDetail(text, "Name:"));
//            extractedData.put("programme", extractDetail(text, "Programme:"));
//            extractedData.put("department", extractDetail(text, "Department:"));
//            extractedData.put("class", extractDetail(text, "Class:"));
//
//            return ResponseEntity.ok(extractedData);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body(null);
//        }
//    }
//
//    private String extractDetail(String text, String fieldName) {
//        int startIndex = text.indexOf(fieldName);
//        if (startIndex != -1) {
//            int endIndex = text.indexOf("\n", startIndex);
//            return text.substring(startIndex + fieldName.length(), endIndex).trim();
//        }
//        return "";
//    }

//    @PostMapping("/upload-pdf")
//    public ResponseEntity<Map<String, String>> uploadPDF(@RequestParam("file") MultipartFile file) {
//        Map<String, String> extractedData = new HashMap<>();
//        try (PDDocument document = PDDocument.load(file.getInputStream())) {
//            PDFTextStripper pdfStripper = new PDFTextStripper();
//            String text = pdfStripper.getText(document);
//
//            // Extract details using regex
//            extractedData.put("name", extractName(text));
//            extractedData.put("programme", extractProgramme(text));
//            extractedData.put("class", extractClass(text));
//
//            return ResponseEntity.ok(extractedData);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body(null);
//        }
//    }
//
//    private String extractName(String text) {
//        // Example regex to find uppercase names
//        Pattern namePattern = Pattern.compile("certify that ([A-Z ]+)");
//        Matcher matcher = namePattern.matcher(text);
//        if (matcher.find()) {
//            return matcher.group(1).trim();
//        }
//        return "Name not found";
//    }
//
//    private String extractProgramme(String text) {
//        // Example regex to find programme near the end of the sentence
//        Pattern programmePattern = Pattern.compile("in (.+)$");
//        Matcher matcher = programmePattern.matcher(text);
//        if (matcher.find()) {
//            return matcher.group(1).trim();
//        }
//        return "Programme not found";
//    }
//
//    private String extractClass(String text) {
//        // Example regex to find class
//        Pattern classPattern = Pattern.compile("with ([A-Z ]+ HONOURS .*)");
//        Matcher matcher = classPattern.matcher(text);
//        if (matcher.find()) {
//            return matcher.group(1).trim();
//        }
//        return "Class not found";
//    }

    @PostMapping("/upload-pdf")
    public ResponseEntity<Map<String, String>> uploadPDF(@RequestParam("file") MultipartFile file) {
        Map<String, String> extractedData = new HashMap<>();
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);

            // Extract details using flexible regex
            extractedData.put("name", extractName(text));
            extractedData.put("programme", extractProgramme(text));
            extractedData.put("class", extractClass(text));

            return ResponseEntity.ok(extractedData);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    private String extractName(String text) {
        // Example regex to find uppercase names
        Pattern namePattern = Pattern.compile("certify that ([A-Z ]+)");
        Matcher matcher = namePattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "Name not found";
    }

    private String extractProgramme(String text) {
        // Flexible regex to capture variations like "Bachelor of Science", "Master of Arts", etc.
        Pattern programmePattern = Pattern.compile("(Bachelor|Master) of [A-Za-z ]+");
        Matcher matcher = programmePattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(0).trim();
        }
        return "Programme not found";
    }

    private String extractClass(String text) {
        // Flexible regex to capture variations of class types
        Pattern classPattern = Pattern.compile("(FIRST CLASS HONOURS|SECOND CLASS HONOURS \\(Upper Division\\)|SECOND CLASS HONOURS \\(Lower Division\\)|THIRD CLASS HONOURS)");
        Matcher matcher = classPattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(0).trim();
        }
        return "Class not found";
    }
}
