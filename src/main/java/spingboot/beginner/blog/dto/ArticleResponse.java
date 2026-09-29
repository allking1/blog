package spingboot.beginner.blog.dto; // Nhớ check lại tên package của bạn

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;
// giống Request nhưng đây là object gửi cho client -> response.
@Data
public class ArticleResponse {

    @NotBlank(message = "Title không được để trống")
    private String id;

    @NotBlank(message = "Title không được để trống")
    private String title;

    @NotBlank(message = "Title không được để trống")
    private String content;

    // Bắt buộc ép format để Jackson không biến nó thành mảng số [2026, 9, 25...]
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime publishDate;

}