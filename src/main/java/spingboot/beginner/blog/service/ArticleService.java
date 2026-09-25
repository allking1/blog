package spingboot.beginner.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spingboot.beginner.blog.model.Article;
import spingboot.beginner.blog.repository.ArticleRepository;

import java.time.LocalDateTime;
import java.util.*;

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

    public Article findByIdPublishedArticle(String id) {
        Optional<Article> article = articleRepository.findById(id);
        if(article.isPresent()) {
            if(!article.get().getPublishDate().isAfter(LocalDateTime.now())) {
                System.out.println("Found article with id: " + id);
                return article.get();
            }
            else {
                throw new IllegalArgumentException("Not Found Article with id: " + id);
            }
        }
        else {
            throw new IllegalArgumentException("Article with id: " + id + " not found");
        }
    }

    public List<Article> findAll() {
        List<Article> articles = articleRepository.findAll();
        articles.sort(Comparator.comparing(Article :: getPublishDate).reversed());
        return articles;
    }

    public List<Article> findAllPublisedArticles() {
        List<Article> articles = articleRepository.findAll();
        List<Article> PublishedArticles = new ArrayList<>();
        for(Article article : articles) {
            if(!article.getPublishDate().isAfter(LocalDateTime.now())) {
                PublishedArticles.add(article);
            }
        }
        PublishedArticles.sort(Comparator.comparing(Article :: getPublishDate).reversed());
        return PublishedArticles; // không phải là null vì đã khởi tạo

    }

    // Trong ArticleService.java

    public void createArticle(String title, String content, LocalDateTime publishDate) {
        // Ép thời gian server bỏ qua phần giây và mili-giây để so khớp với giao diện
        if(title == null || title.isBlank()
        || content == null || content.isBlank()
        || publishDate == null) {
            throw new IllegalArgumentException("Invalid parameters");
        }

        LocalDateTime now = LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.MINUTES);

        if(publishDate.isBefore(now)) {
            throw new IllegalArgumentException("Publish Date cannot be in the past");
        }
        Article article = new Article();
        article.setId(UUID.randomUUID().toString());
        article.setCreatedAt(LocalDateTime.now());
        article.setLastUpdatedAt(LocalDateTime.now());
        article.setPublishDate(publishDate);
        article.setTitle(title);
        article.setContent(content);
        articleRepository.save(article);
    }

    public void updateArticle(String id, String newTitle, String newContent, LocalDateTime newPublishDate) {
        // 1. Kiểm tra đầu vào (Null / Blank)
        if(newTitle == null || newTitle.isBlank()
                || newContent == null || newContent.isBlank()
                || newPublishDate == null) {
            throw new IllegalArgumentException("Title, Content and Publish Date must not be empty");
        }

        Article article = findById(id);
        LocalDateTime now = LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.MINUTES);

        boolean isAlreadyPublished = !article.getPublishDate().isAfter(now);

        // 2. Tách riêng logic xử lý cho Bài đã xuất bản
        if (isAlreadyPublished) {
            if (!newPublishDate.isEqual(article.getPublishDate())) {
                // Báo lỗi cụ thể: Không được đổi ngày của bài đã xuất bản
                throw new IllegalArgumentException("Cannot change the publish date of an already published article");
            }
        }
        // 3. Tách riêng logic xử lý cho Bài chưa xuất bản (Draft)
        else {
            if (newPublishDate.isBefore(now)) {
                // Báo lỗi cụ thể: Ngày xuất bản mới không được nằm ở quá khứ
                throw new IllegalArgumentException("New Publish Date cannot be in the past");
            }
        }

        // 4. Nếu vượt qua mọi lớp bảo vệ ở trên, tiến hành cập nhật
        article.setTitle(newTitle);
        article.setContent(newContent);
        article.setPublishDate(newPublishDate);
        article.setLastUpdatedAt(LocalDateTime.now()); // ĐÃ THÊM DÒNG NÀY (Sửa lỗi bỏ sót)

        articleRepository.save(article);
    }

    public void deleteById(String id) {
        articleRepository.deleteById(id);
    }
}
