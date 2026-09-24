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
    // Trong GuestController.java
    @GetMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("articles", articleService.findAllPublisedArticles());
        return "guest/home"; // Thêm tiền tố thư mục
    }

    @GetMapping("/article/{id}")
    public String articleDetail(@PathVariable String id, Model model) {
        try {
            model.addAttribute("article", articleService.findByIdPublishedArticle(id));
            return "guest/article-detail"; // Thêm tiền tố thư mục
        } catch (IllegalArgumentException e) {
            return "redirect:/home"; // Tạm thời redirect về home nếu gõ sai ID
        }
    }
}