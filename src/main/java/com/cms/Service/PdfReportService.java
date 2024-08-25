package com.cms.Service;

import com.cms.Model.UserActivity;


import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;


import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@Service
public class PdfReportService {
    public byte[] generateUserActivityReport(List<UserActivity> activities) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("User Activity Report"));
        document.add(new Paragraph("\n"));

        Table table = new Table(4);
        table.addCell("User");
        table.addCell("Action");
        table.addCell("Details");
        table.addCell("Timestamp");

        for (UserActivity activity : activities) {
            table.addCell(activity.getUser().getFullName());
            table.addCell(activity.getAction());
            table.addCell(activity.getDetails());
            table.addCell(activity.getTimestamp().toString());
        }

        document.add(table);
        document.close();

        return baos.toByteArray();
    }



    public byte[] generateTableStatsReport(Map<String, Long> tableStats) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Table Statistics Report"));
        document.add(new Paragraph("\n"));

        // iText 7 requires specifying the number of columns in the Table constructor
        Table table = new Table(2);
        table.addCell("Table Name");
        table.addCell("Row Count");

        for (Map.Entry<String, Long> entry : tableStats.entrySet()) {
            table.addCell(entry.getKey());
            table.addCell(entry.getValue().toString());
        }

        document.add(table);
        document.close();

        return baos.toByteArray();
    }
}
