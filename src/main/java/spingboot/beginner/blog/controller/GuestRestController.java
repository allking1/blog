package spingboot.beginner.blog.controller;

import org.springframework.web.bind.annotation.*;
import spingboot.beginner.blog.dto.ArticleResponse;
import spingboot.beginner.blog.model.Article;
import spingboot.beginner.blog.service.ArticleService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/articles")
public class GuestRestController {

    private final ArticleService articleService;

    public GuestRestController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // Hàm tiện ích nội bộ để chuyển đổi Entity -> DTO
    private ArticleResponse convertToResponse(Article article) {
        ArticleResponse response = new ArticleResponse();
        response.setId(article.getId());
        response.setTitle(article.getTitle());
        response.setContent(article.getContent());
        response.setPublishDate(article.getPublishDate());
        return response;
    }

    // Lấy danh sách bài viết đã xuất bản
    @GetMapping
    public List<ArticleResponse> getHomeArticles() {
        List<Article> articles = articleService.findAllPublisedArticles();

        // Dùng Java Stream để lặp và chuyển đổi toàn bộ danh sách sang DTO
        return articles.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Lấy chi tiết 1 bài viết đã xuất bản
    @GetMapping("/{id}")
    public ArticleResponse getArticle(@PathVariable String id) {
        Article article = articleService.findByIdPublishedArticle(id);
        return convertToResponse(article); // Chuyển đổi và trả về
    }
}