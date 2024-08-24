package com.cms.SecurityConfiguration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class PdfProxyController {

    @GetMapping("/proxy/certificates")
    public ResponseEntity<byte[]> getCertificate(@RequestParam String pdfPath) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost" + pdfPath;  // Modify to actual URL

        ResponseEntity<byte[]> response = restTemplate.exchange(
                url, HttpMethod.GET, null, byte[].class);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(response.getBody());
    }
}
