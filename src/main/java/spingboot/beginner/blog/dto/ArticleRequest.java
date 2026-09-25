package spingboot.beginner.blog.dto; // Lưu ý kiểm tra lại chữ "spingboot" theo đúng dự án của bạn

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ArticleRequest {

    @NotBlank(message = "Title không được để trống")
    private String title;

    @NotBlank(message = "Content không được để trống")
    private String content;

    @NotNull(message = "Publish Date không được để trống")
    private LocalDateTime publishDate;
}