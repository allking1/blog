package spingboot.beginner.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spingboot.beginner.blog.model.Article;
import spingboot.beginner.blog.service.ArticleService;


@Controller
@RequestMapping("/admin") // Mọi đường dẫn trong class này đều bắt đầu bằng /admin
public class AdminController {

    private final ArticleService articleService;

    public AdminController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // 1. Dashboard (Hiển thị danh sách)
    @GetMapping({"", "/"}) // Lắng nghe cả /admin và /admin/
    public String dashboard(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "dashboard";
    }

    // 2. Giao diện Thêm mới (New Article)
    @GetMapping("/new")
    public String newArticleForm() {
        return "new-article";
    }

    // 3. Xử lý lưu Thêm mới
    @PostMapping("/new")
    public String processNewArticle(@RequestParam String title, @RequestParam String content) {
        articleService.createArticle(title, content);
        // Sau khi lưu xong, điều hướng (redirect) về lại trang dashboard
        return "redirect:/admin";
    }

    // 4. Giao diện Sửa (Update Article)
    @GetMapping("/edit/{id}")
    public String editArticleForm(@PathVariable String id, Model model) {
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        return "edit-article";
    }

    // 5. Xử lý lưu Sửa
    @PostMapping("/edit/{id}")
    public String processEditArticle(@PathVariable String id, @RequestParam String title, @RequestParam String content) {
        articleService.updateArticle(id, title, content);
        return "redirect:/admin";
    }

    // 6. Xử lý Xóa (Dùng POST để tránh rủi ro bảo mật so với GET)
    @PostMapping("/delete/{id}")
    public String deleteArticle(@PathVariable String id) {
        articleService.deleteById(id);
        return "redirect:/admin";
    }
}
