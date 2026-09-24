package spingboot.beginner.blog.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct; // Nếu dùng Spring Boot < 3.0 thì dùng javax.annotation.PostConstruct
import spingboot.beginner.blog.model.Article;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository // Đánh dấu đây là một Bean đảm nhiệm việc giao tiếp với dữ liệu
public class ArticleRepository {

    private final ObjectMapper objectMapper; // Thư viện của Jackson dùng để parse JSON
    private final Path storageDir;

    // Constructor Injection: Spring sẽ tự động tiêm ObjectMapper và đọc cấu hình từ file properties
    public ArticleRepository(
            ObjectMapper objectMapper,
            @Value("${blog.storage.path}") String storagePathString) {
        this.objectMapper = objectMapper;
        this.storageDir = Paths.get(storagePathString);
    }

    // @PostConstruct giúp hàm này tự động chạy ngay sau khi Spring khởi tạo xong class này
    @PostConstruct
    public void init() {
        try {
            if (!Files.exists(storageDir)) {
                Files.createDirectories(storageDir); // Tự động tạo thư mục nếu chưa có
                System.out.println("Đã tạo thư mục lưu trữ tại: " + storageDir.toAbsolutePath());
            }
        } catch (IOException e) {
            // Fail-fast: Nếu không tạo được thư mục (VD: do thiếu quyền), ném lỗi dừng app luôn
            throw new RuntimeException("Không thể tạo thư mục lưu trữ: " + storageDir, e);
        }
    }

    // 1. Hàm lưu bài viết (Tạo mới hoặc Ghi đè nếu đã tồn tại)
    public void save(Article article) {
        try {
            // Đặt tên file là id.json (VD: 1234-5678.json)
            Path filePath = storageDir.resolve(article.getId() + ".json");
            // Ghi object thành file JSON
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), article);

        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu bài viết ID: " + article.getId(), e);
        }
    }

    // 2. Hàm lấy tất cả bài viết (Dành cho trang chủ / dashboard)
    public List<Article> findAll() {
        List<Article> articles = new ArrayList<>();
        File folder = storageDir.toFile();
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json")); // Chỉ lấy các file .json

        if (files != null) {
            for (File file : files) {
                try {
                    // Đọc nội dung file JSON và biến nó lại thành object Article
                    Article article = objectMapper.readValue(file, Article.class);
                    articles.add(article);
                } catch (IOException e) {
                    System.err.println("Bỏ qua file bị lỗi định dạng: " + file.getName());
                    // Không ném exception ở đây để web vẫn chạy được các bài khác
                }
            }
        }
        return articles; // không phải là null vì đã khởi tạo
    }

    // 3. Hàm lấy 1 bài viết theo ID (Dành cho trang xem chi tiết)
    // Dùng Optional thay vì trả về null để báo hiệu rằng: "Bài viết này có thể không tồn tại"
    public Optional<Article> findById(String id) {
        Path filePath = storageDir.resolve(id + ".json");
        if (Files.exists(filePath)) {
            try {
                Article article = objectMapper.readValue(filePath.toFile(), Article.class);
                return Optional.of(article);
            } catch (IOException e) {
                throw new RuntimeException("Lỗi khi đọc bài viết ID: " + id, e);
            }
        }
        return Optional.empty(); // Không tìm thấy thì trả về rỗng, thay vì return null
    }

    // 4. Hàm xóa bài viết theo ID
    public void deleteById(String id) {
        try {
            Path filePath = storageDir.resolve(id + ".json");
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi xóa bài viết ID: " + id, e);
        }
    }
}