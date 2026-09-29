package spingboot.beginner.blog.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spingboot.beginner.blog.dto.ArticleRequest;
import spingboot.beginner.blog.dto.ArticleResponse;
import spingboot.beginner.blog.model.Article;
import spingboot.beginner.blog.service.ArticleService;

import java.util.List;
import java.util.stream.Collectors;


/*
Controller là tầng đón các Request sau đó gọi các hàm tương ứng của Service để xử lý.
Nhìn chung tầng này có thể điều hướng, validate các dữ liệu nhận được từ Front end trước.
Ở đây có sử dụng ResponseEntity là để kiểm soát kỹ các mã HTTP gửi về cho từng trường hợp.

Cách đặt tên api chuẩn
/api/v1 (version)/admin (authorization/namespace)/articles (resources)/{id} (key).
CHỈ DÙNG danh từ, KHÔNG CÓ ĐỘNG TỪ vì động từ đã được quyết định bởi phương thức.
Nên để danh từ ở dạng số nhiều.
 */
@RestController
@RequestMapping("/api/admin/articles")
public class AdminRestController {

    private final ArticleService articleService;

    public AdminRestController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // Hàm chuyển đổi Entity -> DTO (Giống Guest)
    private ArticleResponse convertToResponse(Article article) {
        ArticleResponse response = new ArticleResponse();
        response.setId(article.getId());
        response.setTitle(article.getTitle());
        response.setContent(article.getContent());
        response.setPublishDate(article.getPublishDate());
        return response;
    }

    // 1. Lấy toàn bộ bài viết (Cả Draft và Published)
    @GetMapping
    public List<ArticleResponse> getAllArticles() {
        List<Article> articles = articleService.findAll();
        return articles.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    // 2. Lấy chi tiết để đổ dữ liệu vào Form Edit
    @GetMapping("/{id}")
    public ArticleResponse getArticleForEdit(@PathVariable String id) {
        Article article = articleService.findById(id);
        return convertToResponse(article);
    }

    // 3. Tạo mới bài viết (Chú ý có thẻ @Valid để kích hoạt kiểm tra DTO)
    @PostMapping
    public ResponseEntity<Void> createArticle(@Valid @RequestBody ArticleRequest request) {
        articleService.createArticle(request.getTitle(), request.getContent(), request.getPublishDate());
        // Trả về mã HTTP 201 (Created) báo hiệu tạo thành công
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 4. Cập nhật bài viết
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateArticle(@PathVariable String id, @Valid @RequestBody ArticleRequest request) {
        articleService.updateArticle(id, request.getTitle(), request.getContent(), request.getPublishDate());
        // Trả về mã HTTP 200 (OK)
        return ResponseEntity.ok().build();
    }

    // 5. Xóa bài viết
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable String id) {
        articleService.deleteById(id);
        // Trả về mã HTTP 204 (No Content) báo hiệu đã xóa và không có data trả về
        return ResponseEntity.noContent().build();
    }
}