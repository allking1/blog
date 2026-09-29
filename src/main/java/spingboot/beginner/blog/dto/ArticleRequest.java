package spingboot.beginner.blog.dto; // Lưu ý kiểm tra lại chữ "spingboot" theo đúng dự án của bạn

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

/*
Trong các hệ thống sử dụng định dạng Json như RestfulAPI thì cần có 1 object đóng gói, vận chuyển các loại data Json
này -> Data Transfer Object. Ở đây khi ta viết 1 lớp có từ khóa @Data, Spring Boot sẽ tự động chuyển đổi Json sang object
này và ngược lại.

Việc sử dụng DTO có thể sẽ tiết kiệm được băng thông, chỉ gửi những thông tin cần gửi.
 */
@Data
public class ArticleRequest {

    @NotBlank(message = "Title không được để trống")
    private String title;

    @NotBlank(message = "Content không được để trống")
    private String content;

    @NotNull(message = "Publish Date không được để trống")
    private LocalDateTime publishDate;
}