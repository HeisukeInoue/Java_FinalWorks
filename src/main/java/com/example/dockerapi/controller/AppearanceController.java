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
import com.example.dockerapi.dto.apiResponse;
import com.example.dockerapi.dto.appearanceRequest;
import com.example.dockerapi.model.appearance;
import com.example.dockerapi.model.appearanceDetail;
import com.example.dockerapi.service.appearanceService;

@RestController
@RequestMapping("/api/appearance")
public class appearanceController {

    @Autowired
    private  appearanceService appearanceService;

    /*タレント出演情報の一覧を取得する*/
    @GetMapping
    public ResponseEntity<apiResponse<List<appearance>>> getAllappearance() {
        try {
            List<appearance> appearance = appearanceService.getAllappearance();
            return ResponseEntity.ok(apiResponse.success(appearance));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(apiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    }

    /*タレント出演情報の詳細を取得する*/
    @GetMapping("/{id}")
    public ResponseEntity<apiResponse<appearanceDetail>> getappearanceDetail(@PathVariable int id) {
        try {
            appearanceDetail detail = appearanceService.getappearanceDetail(id);
            if (detail == null) {
                return ResponseEntity.status(404).body(apiResponse.error("出演情報が見つかりません"));
            }
            return ResponseEntity.ok(apiResponse.success(detail));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(apiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    }

    /*タレント出演情報を投稿する*/
@PostMapping
public ResponseEntity<apiResponse<String>> postNewappearance(@RequestBody appearanceRequest request) {
    try {
        Date date = request.getDate();
        String title = request.getTitle();
        String text = request.getText();
        appearanceService.postNewappearance(date, title, text);
        return ResponseEntity.status(201).body(apiResponse.success("出演情報を登録しました"));
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500)
            .body(apiResponse.error("エラーが発生しました: " + e.getMessage()));
    }
}

    /*出演情報を編集する*/
    @PutMapping("/{id}")
    public ResponseEntity<apiResponse<appearance>> updateappearance(@RequestBody appearanceRequest request, @PathVariable int id) {
        try {
            Date date = request.getDate();
            String title = request.getTitle();
            String text = request.getText();
            int result = appearanceService.updateappearance(date, title, text, id);
            if (result == 0){
                return ResponseEntity.status(404).body(apiResponse.error("出演情報が見つかりません"));
            }
            appearance updated = appearanceService.getappearanceDetail(id);
            return ResponseEntity.ok(apiResponse.success(updated));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(apiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    }

    /*出演情報を削除する*/
    @DeleteMapping("/{id}")
    public ResponseEntity<apiResponse<Void>> deleteappearance(@PathVariable int id) {
        try {
            int result = appearanceService.deleteappearance(id);
            if (result == 0){
                return ResponseEntity.status(404)
                    .body(apiResponse.error("出演情報が見つかりません"));
            }
            return ResponseEntity.ok(apiResponse.success(null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                .body(apiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    }

}