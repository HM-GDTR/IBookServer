package org.rainbowx.javaserver.DAO;

import org.rainbowx.javaserver.Bean.Article;
import org.rainbowx.javaserver.Bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    Article findArticleByAid(int aid);
    @SuppressWarnings("unused")
    List<Article> findArticlesByAuthor(User author);
    List<Article> findArticlesByAidNotNull();
}
