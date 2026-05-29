package com.example.sreepooja.Utility;

import com.example.sreepooja.ExceptionHandlers.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Component
public class FileStorageUtil {

    @Value("${app.upload.base-path}")
    private String basePath;

    public String saveFile(String folder, MultipartFile file) {

        try {

            Path uploadDirectory = Paths.get(basePath, folder);

            if (!Files.exists(uploadDirectory)) {
                Files.createDirectories(uploadDirectory);
            }

            String extension =
                    getFileExtension(file.getOriginalFilename());

            String fileName =
                    System.currentTimeMillis()
                            + "_"
                            + UUID.randomUUID()
                            .toString()
                            .replace("-", "")
                            + "."
                            + extension;

            Path targetPath =
                    uploadDirectory.resolve(fileName);

            Files.copy(
                    file.getInputStream(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return "/uploads/"
                    + folder
                    + "/"
                    + fileName;

        } catch (IOException ex) {

            throw new FileStorageException(
                    "Failed to store file",
                    ex
            );
        }
    }

    public void deleteFile(String fileUrl) {

        if (fileUrl == null || fileUrl.isBlank()) {
            return;
        }

        try {

            String relativePath =
                    fileUrl.replace("/uploads/", "");

            Path filePath =
                    Paths.get(basePath, relativePath);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }

        } catch (IOException ex) {

            throw new FileStorageException(
                    "Failed to delete file : " + fileUrl,
                    ex
            );
        }
    }

    private String getFileExtension(
            String originalFileName) {

        if (originalFileName == null
                || !originalFileName.contains(".")) {

            return "";
        }

        return originalFileName.substring(
                originalFileName.lastIndexOf(".") + 1
        );
    }
}