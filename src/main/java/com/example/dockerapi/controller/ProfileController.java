package com.example.dockerapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.dockerapi.dto.ApiResponse;
import com.example.dockerapi.dto.ProfileRequest;
import com.example.dockerapi.model.Profile;
import com.example.dockerapi.service.ProfileService;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    
    @Autowired
    private ProfileService profileService;

    /*タレント個別プロフィールを取得（画像URL/その他プロフィール共に）*/
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Profile>> getProfile(@PathVariable int id) {
        try {
            //String imageUrl = profileService.getImageLink(id);
            Profile profile = profileService.getProfile(id);
            if (profile == null) {
                return ResponseEntity.status(404)
                    .body(ApiResponse.error("タレント情報が見つかりません"));
            }
            return ResponseEntity.ok(ApiResponse.success(profile));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                .body(ApiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    }

    /*個別にプロフィール情報を編集する(画像以外)*/
    @PutMapping("/{id}/info")
    public ResponseEntity<ApiResponse<Profile>> updateProfile(@RequestBody ProfileRequest request, @PathVariable int id) {
        try {
            String image_link = request.getImageLink();
            String name = request.getName();
            int age = request.getAge();
            String from = request.getFrom();
            int height = request.getHeight();
            String hobby = request.getHobby();
            
            int result = profileService.updateProfile(image_link, name, age, from, height, hobby, id);
            if (result == 0){
                return ResponseEntity.status(404).body(ApiResponse.error("タレントが見つかりません"));
            }
            /*更新後のプロフィールを取得する*/
            Profile updatedProfile = profileService.getProfile(id);
            return ResponseEntity.ok(ApiResponse.success(updatedProfile));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    }

}