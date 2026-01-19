package com.example.dockerapi.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.dockerapi.dto.ApiResponse;
import com.example.dockerapi.dto.FileUploadForm;
import com.example.dockerapi.model.Profile;
import com.example.dockerapi.service.FileUploadService;
import com.example.dockerapi.service.ProfileService;

@RestController
@RequestMapping("/api/profile")
public class FileController {
    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private ProfileService profileService;
    
    @PutMapping("/{id}/image")
    public ResponseEntity<ApiResponse<Profile>> uploadImage(
            @RequestPart("image_link") MultipartFile file, @PathVariable int id) {
        try {
            // FileUploadForm を作成
            FileUploadForm form = new FileUploadForm();
            form.setMultipartFile(file);
            form.setCreateAt(java.time.LocalDateTime.now());
            
            String bucketName = "java-finalwork";
            // S3にアップロードしてURLを取得
            String imageUrl = fileUploadService.fileUpload(form, bucketName);
        
            // DBにURLを保存
            profileService.updateImageLink(imageUrl, id);

            /*更新後のプロフィールを取得する*/
            Profile updatedProfile = profileService.getProfile(id);
            return ResponseEntity.ok(ApiResponse.success(updatedProfile));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                .body(ApiResponse.error("ファイルアップロードに失敗しました: " + e.getMessage()));
        }
    }
}
