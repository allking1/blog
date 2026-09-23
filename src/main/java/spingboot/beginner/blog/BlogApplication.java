package spingboot.beginner.blog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.yaml.snakeyaml.tokens.ScalarToken;
import spingboot.beginner.blog.model.Article;
import spingboot.beginner.blog.repository.ArticleRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootApplication
public class BlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Đăng ký module để Jackson hiểu được kiểu LocalDateTime
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }


    }

