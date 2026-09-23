package spingboot.beginner.blog.controller;

import spingboot.beginner.blog.model.Article;
import spingboot.beginner.blog.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class GuestController {

    private final ArticleService articleService;

    public GuestController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }
    // 1. Trang chủ: Hiển thị tất cả bài viết
    @GetMapping("/home")
    public String homePage(Model model) {
        // Lấy danh sách đã sắp xếp từ Service
        var articles = articleService.findAll();

        // Nhét dữ liệu vào Model để Thymeleaf có thể lấy ra dùng
        model.addAttribute("articles", articles);

        // Trả về tên của file giao diện (sẽ trỏ tới file templates/home.html)
        return "home";
    }

    // 2. Trang chi tiết: Xem 1 bài viết
    @GetMapping("/article/{id}")
    public String articleDetail(@PathVariable String id, Model model) {
        try {
            Article article = articleService.findById(id);
            model.addAttribute("article", article);
            return "article-detail"; // Trỏ tới templates/article-detail.html
        } catch (IllegalArgumentException e) {
            // Nếu không tìm thấy ID, trả về trang lỗi 404
            return "404 : Not Found";
        }
    }
}