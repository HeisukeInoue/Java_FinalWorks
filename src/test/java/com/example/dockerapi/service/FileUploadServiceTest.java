package com.example.dockerapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import com.example.dockerapi.dto.FileUploadForm;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@ExtendWith(MockitoExtension.class)
public class FileUploadServiceTest {
    @Mock
    S3Client s3Client;

    @InjectMocks
    private FileUploadService fileUploadService;

    /*ファイルアップロードテスト(正常系)*/
    @Test
    void testFileUpload() throws IOException {
        FileUploadForm fileUploadForm = new FileUploadForm();
        fileUploadForm.setMultipartFile(new MockMultipartFile("mainimages.png", "mainimages.png", "image/png", "test content".getBytes()));
        fileUploadForm.setCreateAt(LocalDateTime.now());
        String bucketName = "java-finalwork";
        
        String imageUrl = fileUploadService.fileUpload(fileUploadForm, bucketName);
        
        // PutObjectRequestの内容を検証（バケット名とキー）
        verify(s3Client).putObject(
            argThat((PutObjectRequest request) -> 
                request.bucket().equals(bucketName) && 
                request.key().equals("mainimages.png")
            ),
            any(RequestBody.class) // RequestBodyはany()で検証
        );
        
        assertNotNull(imageUrl);
        assertEquals("https://java-finalwork.s3.ap-southeast-2.amazonaws.com/mainimages.png", imageUrl);
    }

    /*ファイルアップロードテスト(異常系)*/
    @Test
    void testFileUpload_InvalidFile() throws IOException {
        FileUploadForm fileUploadForm = new FileUploadForm();
        fileUploadForm.setMultipartFile(new MockMultipartFile("mainimages.png", "mainimages.png", "image/ng", "test content".getBytes()));
        fileUploadForm.setCreateAt(LocalDateTime.now());
        String bucketName = "java-finalwork";
        
        // S3Exceptionをスローさせる
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
            .thenThrow(S3Exception.builder().message("Invalid file type").build());
        
        // IOExceptionがスローされることを期待
        assertThrows(IOException.class, () -> {
            fileUploadService.fileUpload(fileUploadForm, bucketName);
        });
    }
}