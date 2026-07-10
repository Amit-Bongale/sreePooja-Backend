package com.example.sreepooja.Service.File;

import com.example.sreepooja.DTO.Response.File.FileUploadResponse;
import com.example.sreepooja.Enum.File.FileType;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    FileUploadResponse uploadImage(
            MultipartFile file,
            FileType fileType
    );

    FileUploadResponse uploadDocument(
            MultipartFile file,
            FileType fileType
    );

    void deleteImage(String fileUrl);
}