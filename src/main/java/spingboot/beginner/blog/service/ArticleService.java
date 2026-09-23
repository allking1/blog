package spingboot.beginner.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spingboot.beginner.blog.model.Article;
import spingboot.beginner.blog.repository.ArticleRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;


    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article findById(String id) {
        Optional<Article> article = articleRepository.findById(id);
        if(article.isPresent()) {
            System.out.println("Found article with id: " + id + " " + article.get().getTitle());
            return article.get();
        }
        else {
            throw new IllegalArgumentException("Article with id: " + id + " not found");
        }
    }

    public List<Article> findAll() {
        List<Article> articles = articleRepository.findAll();
        articles.sort(Comparator.comparing(Article :: getTime).reversed());
        return articles;
    }

    public void createArticle(String title, String content) {
        Article article = new Article();
        article.setId(UUID.randomUUID().toString()); // Service tự sinh ID
        article.setTime(LocalDateTime.now());        // Service tự gán giờ
        article.setTitle(title);
        article.setContent(content);

        articleRepository.save(article);
    }


    public void updateArticle(String id, String newTitle, String newContent) {
        // Tìm bài cũ (nếu không có sẽ tự văng lỗi từ hàm findById)
        Article existingArticle = findById(id);

        // Chỉ cập nhật tiêu đề và nội dung, GIỮ NGUYÊN ID và Time cũ
        existingArticle.setTitle(newTitle);
        existingArticle.setContent(newContent);

        articleRepository.save(existingArticle);
    }
    public void deleteById(String id) {
        articleRepository.deleteById(id);
    }
}
