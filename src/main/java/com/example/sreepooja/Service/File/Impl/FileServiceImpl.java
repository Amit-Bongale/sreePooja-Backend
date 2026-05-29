package com.example.sreepooja.Service.File.Impl;

import com.example.sreepooja.DTO.Response.File.FileUploadResponse;
import com.example.sreepooja.Enum.File.FileType;
import com.example.sreepooja.ExceptionHandlers.FileSizeExceededException;
import com.example.sreepooja.ExceptionHandlers.InvalidFileException;
import com.example.sreepooja.Service.File.FileService;
import com.example.sreepooja.Utility.FileStorageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private static final long MAX_FILE_SIZE =
            2 * 1024 * 1024;

    private static final Set<String> ALLOWED_CONTENT_TYPES =
            Set.of(
                    "image/jpeg",
                    "image/png",
                    "image/jpg"
            );

    private final FileStorageUtil fileStorageUtil;

    @Override
    public FileUploadResponse uploadImage(
            MultipartFile file,
            FileType fileType) {

        validateFile(file);

        String fileUrl =
                fileStorageUtil.saveFile(
                        fileType.getFolderName(),
                        file
                );

        String fileName =
                fileUrl.substring(
                        fileUrl.lastIndexOf("/") + 1
                );

        return FileUploadResponse.builder()
                .fileName(fileName)
                .fileUrl(fileUrl)
                .fileSize(file.getSize())
                .build();
    }

    @Override
    public void deleteImage(String fileUrl) {

        fileStorageUtil.deleteFile(fileUrl);
    }

    private void validateFile(
            MultipartFile file) {

        if (file == null || file.isEmpty()) {

            throw new InvalidFileException(
                    "Image file is required"
            );
        }

        if (file.getSize() > MAX_FILE_SIZE) {

            throw new FileSizeExceededException(
                    "Maximum allowed image size is 2 MB"
            );
        }

        String fileName =
                file.getOriginalFilename();

        if (fileName == null ||
                !(fileName.toLowerCase().endsWith(".jpg")
                        || fileName.toLowerCase().endsWith(".jpeg")
                        || fileName.toLowerCase().endsWith(".png"))) {

            throw new InvalidFileException(
                    "Invalid image type. Only JPG, JPEG and PNG images are allowed"
            );
        }

        if (!ALLOWED_CONTENT_TYPES.contains(
                file.getContentType())) {

            throw new InvalidFileException(
                    "Invalid image type. Only JPG, JPEG and PNG images are allowed"
            );
        }
    }
}