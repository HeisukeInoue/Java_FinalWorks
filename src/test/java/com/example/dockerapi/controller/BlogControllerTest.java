package com.example.dockerapi.controller;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.example.dockerapi.model.Blog;
import com.example.dockerapi.service.BlogService;


@WebMvcTest(BlogController.class)
class BlogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogService blogService;

    /*個別にブログ記事を取得・閲覧する 正常系テスト*/
    @Test
    void testGetBlogById_success() throws Exception {
        // モックの戻り値を設定
        Blog dummyBlog = new Blog(1, 1, "タイトル", "内容", null, null, null);
        Mockito.when(blogService.getBlogById(1)).thenReturn(dummyBlog);

        mockMvc.perform(get("/api/blogs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("タイトル"))
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    /*個別にブログ記事を取得・閲覧する 異常系テスト*/
    @Test
    void testGetBlogById_notFound() throws Exception {
        Mockito.when(blogService.getBlogById(1)).thenReturn(null);

        mockMvc.perform(get("/api/blogs/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error").value("ブログが見つかりません"));
    }
}

