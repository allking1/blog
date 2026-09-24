package spingboot.beginner.blog.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spingboot.beginner.blog.model.Article;
import spingboot.beginner.blog.service.ArticleService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;


@Controller
@RequestMapping("/admin") // Mọi đường dẫn trong class này đều bắt đầu bằng /admin
public class AdminController {

    private final ArticleService articleService;

    public AdminController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // Trong AdminController.java (nhớ import org.springframework.format.annotation.DateTimeFormat;)

    @GetMapping({"", "/"})
    public String dashboard(Model model) {
        model.addAttribute("articles", articleService.findAll());
        // Bỏ dấu gạch chéo ở đầu đi, Thymeleaf không cần nó
        return "admin/dashboard";
    }

    @GetMapping("/article/{id}")
    public String article(@PathVariable String id, Model model) {
        model.addAttribute("article", articleService.findById(id));
        return "admin/article-detail";
    }

    @GetMapping("/new")
    public String newArticleForm() {
        return "admin/new-article";
    }

    @PostMapping("/new")
    public String processNewArticle(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime publishDate) { // ÉP KIỂU Ở ĐÂY
        articleService.createArticle(title, content, publishDate);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editArticleForm(@PathVariable String id, Model model) {
        Article article = articleService.findById(id);
        model.addAttribute("article", article);

        // TÍNH TOÁN XEM BÀI ĐÃ PUBLISH CHƯA RỒI TRUYỀN XUỐNG GIAO DIỆN
        boolean isPublished = !article.getPublishDate().isAfter(LocalDateTime.now());
        model.addAttribute("isPublished", isPublished);

        return "admin/edit-article";
    }

    @PostMapping("/edit/published/{id}")
    public String processPublisedEditArticle(@PathVariable String id, @RequestParam String title, @RequestParam String content) {
        articleService.updateArticle(id, title, content);
        return "redirect:/admin";
    }

    @PostMapping("/edit/unPublished/{id}")
    public String processNotPublishedArticle(
            @PathVariable String id,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime publishDate) { // ÉP KIỂU Ở ĐÂY
        articleService.updateArticle(id, title, content, publishDate);
        return "redirect:/admin";
    }
    @PostMapping("/delete/{id}")
    public String deleteArticle(@PathVariable String id) {
        articleService.deleteById(id);
        return "redirect:/admin";
    }
}
