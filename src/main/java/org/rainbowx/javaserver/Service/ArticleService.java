package org.rainbowx.javaserver.Service;

import org.rainbowx.javaserver.Bean.Article;
import org.rainbowx.javaserver.Bean.User;
import org.rainbowx.javaserver.DAO.ArticleRepository;
import org.rainbowx.javaserver.DAO.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ArticleService {
    private static final String TAG = "ArticleService";
    protected static Logger logger;
    protected UserRepository userRepository;
    protected ArticleRepository articleRepository;

    static {
        logger = Logger.getLogger(TAG);
    }

    @Autowired
    public ArticleService(ArticleRepository repository, UserRepository repository1) {
        this.userRepository = repository1;
        this.articleRepository = repository;
    }

    /**
     * 发布文章
     * @param author 文章作者
     * @param title 文章标题
     * @param content 文章内容
     * @return 如果成功返回aid, 否则返回-1
     */
    public int release(int author, String title, String content) {
        try {
            User user = userRepository.findUserByUid(author);

            Article article = new Article();
            article.setAuthor(user);
            article.setTitle(title);
            article.setTime(LocalDateTime.now());
            article.setContent(content);
            article = articleRepository.save(article);
            return article.getAid();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE,"Error: ",e);
            return -1;
        }
    }

    /**
     * 查询文章
     * @return 如果成功返回文章列表, 否则返回null
     */
    public List<Article> query() {
        try {
            return articleRepository.findArticlesByAidNotNull();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE,"Error: ",e);
            return null;
        }
    }

    /**
     * 修改文章内容
     * @param aid 文章id
     * @param content 新的文章内容
     * @return 如果成功返回0, 否则返回-1
     */
    public int modify(int aid, String content) {
        try {
            Article article = articleRepository.findArticleByAid(aid);
            article.setContent(content);
            article = articleRepository.save(article);
            if(article.getContent().equals(content)) return 0;
            else throw new RuntimeException("内容没有被修改");
        }
        catch (Exception e) {
            logger.log(Level.SEVERE,"Error: ",e);
            return -1;
        }
    }

    /**
     * 删除文章
     * @param aid 需要删除的文章id
     * @return 如果成功返回0, 否则返回-1
     */
    public int delete(int aid) {
        try {
            Article article = articleRepository.findArticleByAid(aid);
            articleRepository.delete(article);
            return 0;
        }
        catch (Exception e) {
            logger.log(Level.SEVERE,"Error: ",e);
            return -1;
        }
    }
}
