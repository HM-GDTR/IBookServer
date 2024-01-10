package org.rainbowx.javaserver.Service;

import org.rainbowx.javaserver.Bean.Article;
import org.rainbowx.javaserver.Bean.Remark;
import org.rainbowx.javaserver.Bean.User;
import org.rainbowx.javaserver.DAO.ArticleRepository;
import org.rainbowx.javaserver.DAO.ChatRepository;
import org.rainbowx.javaserver.DAO.RemarkRepository;
import org.rainbowx.javaserver.DAO.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class RemarkService {
    private static final String TAG = "RemarkService";
    protected static Logger logger;
    protected UserRepository userRepository;
    protected RemarkRepository remarkRepository;
    protected ArticleRepository articleRepository;

    static {
        logger = Logger.getLogger(TAG);
    }

    @Autowired
    public RemarkService(RemarkRepository repository, ArticleRepository repository1, UserRepository repository2) {
        this.userRepository = repository2;
        this.remarkRepository = repository;
        this.articleRepository = repository1;
    }

    /**
     * 为文章添加一条评论
     * @param aid 文章id
     * @param author 评论作者id
     * @param content 评论内容
     * @return 如果成功返回评论id, 否则返回-1
     */
    public int release(int aid, int author, String content) {
        try {
            User user = userRepository.findUserByUid(author);
            if(user == null) throw new RuntimeException("没有此用户: " + author);

            Article article = articleRepository.findArticleByAid(aid);

            Remark remark = new Remark();
            remark.setAuthor(user);
            remark.setArticle(article);
            remark.setTime(LocalDateTime.now());
            remark.setContent(content);
            remark = remarkRepository.save(remark);
            return remark.getRid();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE,"Error: ",e);
            return -1;
        }
    }

    /**
     * 寻找某篇文章的所有评论
     * @param aid 文章id
     * @return 如果成功返回评论列表, 否则返回null
     */
    public List<Remark> queryByAid(int aid) {
        try {
            Article article = articleRepository.findArticleByAid(aid);
            return remarkRepository.findRemarksByArticle(article);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE,"Error: ",e);
            return null;
        }
    }

    /**
     * 根据评论id修改评论内容
     * @param rid 要修改的评论id
     * @param content 修改后的评论内容
     * @return 如果成功返回0, 否则返回-1
     */
    public int modify(int rid, String content) {
        try {
            Remark remark = remarkRepository.findRemarkByRid(rid);
            remark.setContent(content);
            return 0;
        }
        catch (Exception e) {
            logger.log(Level.SEVERE,"Error: ",e);
            return -1;
        }
    }

    /**
     * 删除评论
     * @param rid 评论id
     * @return 如果成功返回0, 否则返回-1
     */
    public int delete(int rid) {
        try {
            Remark remark = remarkRepository.findRemarkByRid(rid);
            remarkRepository.delete(remark);
            return 0;
        }
        catch (Exception e) {
            logger.log(Level.SEVERE,"Error: ",e);
            return -1;
        }
    }
}
