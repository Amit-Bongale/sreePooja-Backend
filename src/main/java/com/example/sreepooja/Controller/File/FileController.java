package com.example.sreepooja.Controller.File;

import com.example.sreepooja.DTO.Response.File.FileUploadResponse;
import com.example.sreepooja.Enum.File.FileType;
import com.example.sreepooja.Service.File.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") FileType fileType) {

        return ResponseEntity.ok(
                fileService.uploadImage(
                        file,
                        fileType
                )
        );
    }
}