package com.example.dockerapi.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.example.dockerapi.model.Appearance;
import com.example.dockerapi.service.AppearanceService;


@WebMvcTest(AppearanceController.class)
public class AppearanceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppearanceService appearanceService;

    /*タレント出演情報の一覧を取得する 正常系テスト*/
    @Test
    void testGetAllAppearance() throws Exception {

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        /*モックの戻り値を設定*/
        List<Appearance> dummyAppearance = new ArrayList<>();
        dummyAppearance.add(new Appearance(1, 1, date, "出演タイトル1", LocalDateTime.now(), LocalDateTime.now()));
        dummyAppearance.add(new Appearance(2, 1, date, "出演タイトル2", LocalDateTime.now(), LocalDateTime.now()));
        when(appearanceService.getAllAppearance()).thenReturn(dummyAppearance);

        /*HTTPリクエストをシミュレート*/
        mockMvc.perform(get("/api/appearance"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(2));
    }

    /*タレント出演情報の一覧を取得する 異常系テスト*/
    @Test
    void testGetAllAppearance_error() throws Exception {
        when(appearanceService.getAllAppearance()).thenReturn(null);
        mockMvc.perform(get("/api/appearance"))
        .andExpect(jsonPath("$.data").doesNotExist())
        .andExpect(jsonPath("$.error").doesNotExist());
    }
}