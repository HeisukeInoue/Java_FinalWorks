package com.example.dockerapi.controller;

import java.sql.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.dockerapi.dto.ApiResponse;
import com.example.dockerapi.dto.AppearanceRequest;
import com.example.dockerapi.model.Appearance;
import com.example.dockerapi.model.AppearanceDetail;
import com.example.dockerapi.service.AppearanceService;

@RestController
@RequestMapping("/api/appearance")
public class AppearanceController {

    @Autowired
    private AppearanceService appearanceService;

    /*タレント出演情報の一覧を取得する*/
    @GetMapping
    public ResponseEntity<ApiResponse<List<Appearance>>> getAllAppearance() {
        try {
            List<Appearance> appearance = appearanceService.getAllAppearance();
            return ResponseEntity.ok(ApiResponse.success(appearance));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    }

    /*タレント出演情報の詳細を取得する*/
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AppearanceDetail>> getAppearanceDetail(@PathVariable int id) {
        try {
            AppearanceDetail detail = appearanceService.getAppearanceDetail(id);
            if (detail == null) {
                return ResponseEntity.status(404).body(ApiResponse.error("出演情報が見つかりません"));
            }
            return ResponseEntity.ok(ApiResponse.success(detail));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    }

    /*タレント出演情報を投稿する*/
@PostMapping
public ResponseEntity<ApiResponse<String>> postNewAppearance(@RequestBody AppearanceRequest request) {
    try {
        Date date = request.getDate();
        String title = request.getTitle();
        String text = request.getText();
        appearanceService.postNewAppearance(date, title, text);
        return ResponseEntity.status(201).body(ApiResponse.success("出演情報を登録しました"));
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500)
            .body(ApiResponse.error("エラーが発生しました: " + e.getMessage()));
    }
}

    /*出演情報を編集する*/
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AppearanceDetail>> updateAppearance(@RequestBody AppearanceRequest request, @PathVariable int id) {
        try {
            Date date = request.getDate();
            String title = request.getTitle();
            String text = request.getText();
            int result = appearanceService.updateAppearance(date, title, text, id);
            if (result == 0){
                return ResponseEntity.status(404).body(ApiResponse.error("出演情報が見つかりません"));
            }
            AppearanceDetail updated = appearanceService.getAppearanceDetail(id);
            return ResponseEntity.ok(ApiResponse.success(updated));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    }

    /*出演情報を削除する*/
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAppearance(@PathVariable int id) {
        try {
            int result = appearanceService.deleteAppearance(id);
            if (result == 0){
                return ResponseEntity.status(404)
                    .body(ApiResponse.error("出演情報が見つかりません"));
            }
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                .body(ApiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    }
    
}