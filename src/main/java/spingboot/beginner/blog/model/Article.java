package spingboot.beginner.blog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
// lombok tự động tạo các getter, setter, constructor
@Data
@NoArgsConstructor
@AllArgsConstructor
// POJO
public class Article {
    private String id;
    private String title;
    private String content;
    private LocalDateTime time; // createTime and updateTime
}
