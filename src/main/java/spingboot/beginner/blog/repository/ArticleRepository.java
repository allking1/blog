package spingboot.beginner.blog.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import spingboot.beginner.blog.model.Article;

@Repository
public interface ArticleRepository extends CrudRepository<Article, String> {

}
