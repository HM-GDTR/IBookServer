package org.rainbowx.javaserver.Bean;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "remark")
public class Remark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rid;
    @ManyToOne
    @JoinColumn(name = "article", referencedColumnName = "aid")
    private Article article;
    private LocalDateTime time;
    @ManyToOne
    @JoinColumn(name = "author", referencedColumnName = "uid")
    private User author;
    @Column(columnDefinition = "TEXT")
    private String content;

    @SuppressWarnings("unused")
    protected Remark(int rid, Article article, LocalDateTime time, User author, String content) {
        this.rid = rid;
        this.article = article;
        this.time = time;
        this.author = author;
        this.content = content;
    }

    public Remark() {
    }

    public int getRid() {
        return rid;
    }

    @SuppressWarnings("unused")
    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
