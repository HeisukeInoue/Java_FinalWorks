package com.example.dockerapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.dockerapi.model.Profile;
import com.example.dockerapi.repository.ProfileRepository;

@Service
public class ProfileService {
    
    @Autowired
    private ProfileRepository profileRepository;

    /*タレント個別プロフィールを取得（画像URL/その他プロフィール共に）*/
    public Profile getProfile(int id) {
        return profileRepository.getProfile(id);
    }

    /*個別にプロフィール情報を編集する(画像以外)*/
    public int updateProfile(String image_link, String name, int age, String from, int height, String hobby, int id) {
        return profileRepository.updateProfile(image_link, name, age, from, height, hobby, id);
    }
    
    /*プロフィール画像を更新*/
    public int updateImageLink(String url, int id) {
        return profileRepository.updateImageLink(url, id);
    }

    /*プロフィール画像URLを取得*/
    public String getImageLink(int id) {
        return profileRepository.getImageLink(id);
    }
}