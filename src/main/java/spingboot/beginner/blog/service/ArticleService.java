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
        Article article = findById(id);
        LocalDateTime now = LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.MINUTES);

        // LỚP BẢO VỆ BACKEND: Chặn Hacker gọi api đổi giờ khi bài đã publish
        if (!article.getPublishDate().isAfter(now)) {
            throw new IllegalStateException("Article is already published, cannot change date.");
        }

        if(newPublishDate.isBefore(now)) {
            throw new IllegalArgumentException("Publish Date cannot be in the past");
        }

        article.setTitle(newTitle);
        article.setContent(newContent);
        article.setLastUpdatedAt(LocalDateTime.now());
        article.setPublishDate(newPublishDate);
        articleRepository.save(article);
    }

    public void updateArticle(String id, String newTitle, String newContent) {
        LocalDateTime now = LocalDateTime.now();
        Article article = findById(id);
        article.setLastUpdatedAt(now);
        article.setTitle(newTitle);
        article.setContent(newContent);
        articleRepository.save(article);
        System.out.println("Updated article with id: " + article.getId());
    }
    public void deleteById(String id) {
        articleRepository.deleteById(id);
    }
}
