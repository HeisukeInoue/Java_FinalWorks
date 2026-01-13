package com.example.dockerapi.controller;
import java.util.List;
// import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.dockerapi.model.Appearance;
// import com.example.dockerapi.dto.BlogListResponse;
// import com.example.dockerapi.model.Blog;
import com.example.dockerapi.repository.AppearanceRepository;
import com.example.dockerapi.service.AppearanceService;

@RestController
@RequestMapping("/api/appearance")
public class AppearanceController {

    @Autowired
        private AppearanceService AppearanceService;
    @Autowired
        private AppearanceRepository AppearanceRepository;

    /*タレント出演情報の一覧を取得する*/
    @GetMapping
    public ResponseEntity<?> getAllAppearance() {
        try {
            List<Appearance> appearance = AppearanceService.getAllAppearance();
            return ResponseEntity.ok(appearance);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("エラーが発生しました");
        }
    }
}
