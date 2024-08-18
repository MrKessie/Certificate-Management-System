package com.cms.Service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path fileStorageLocation = Paths.get("path/to/your/xampp/htdocs").toAbsolutePath().normalize();

    public String storeFile(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path targetLocation = fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation);
            return targetLocation.toString();
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename(), ex);
        }
    }
}
