package com.example.dockerapi.controller;

import java.sql.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.dockerapi.dto.AppearanceRequest;
import com.example.dockerapi.model.Appearance;
import com.example.dockerapi.model.AppearanceDetail;
import com.example.dockerapi.service.AppearanceService;

@RestController
@RequestMapping("/api/appearance")
public class AppearanceController {

    @Autowired
    private  AppearanceService appearanceService;

    /*タレント出演情報の一覧を取得する*/
    @GetMapping
    public ResponseEntity<?> getAllAppearance() {
        try {
            List<Appearance> appearance = appearanceService.getAllAppearance();
            return ResponseEntity.ok(appearance);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("エラーが発生しました");
        }
    }

    /*タレント出演情報の詳細を取得する*/
    @GetMapping("/{id}")
    public ResponseEntity<?> getAppearanceDetail(@PathVariable int id) {
        try {
            AppearanceDetail detail = appearanceService.getAppearanceDetail(id);
            return ResponseEntity.ok(detail);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("エラーが発生しました");
        }
    }

    /*タレント出演情報を投稿する*/
    @PostMapping
    public ResponseEntity<?> postNewAppearance(@RequestBody AppearanceRequest request) { //リクエストをdto"AppearanceRequest"へ
        try {
            Date date = request.getDate();
            String title = request.getTitle();
            String text = request.getText();
            appearanceService.postNewAppearance(date, title, text);
            return ResponseEntity.status(201).body("出演情報を登録しました");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("エラーが発生しました");
        }
    }
}