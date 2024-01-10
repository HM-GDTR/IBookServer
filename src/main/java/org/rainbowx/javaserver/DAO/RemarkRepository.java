package org.rainbowx.javaserver.DAO;

import org.rainbowx.javaserver.Bean.Article;
import org.rainbowx.javaserver.Bean.Remark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RemarkRepository extends JpaRepository<Remark, Integer> {
    Remark findRemarkByRid(int rid);
    List<Remark> findRemarksByArticle(Article article);
}
