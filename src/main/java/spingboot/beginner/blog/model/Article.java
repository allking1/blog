package spingboot.beginner.blog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
// lombok tự động tạo các getter, setter, constructor
@Data
@NoArgsConstructor
@AllArgsConstructor
// POJO
@Entity
@Table(name = "articles")
public class Article {
    @Id
    private String id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime lastUpdatedAt;
    @Column(nullable = false)
    private LocalDateTime PublishDate;

}
