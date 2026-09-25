package spingboot.beginner.blog.dto; // Nhớ check lại tên package của bạn

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ArticleResponse {

    private String id;
    private String title;
    private String content;

    // Bắt buộc ép format để Jackson không biến nó thành mảng số [2026, 9, 25...]
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime publishDate;

}