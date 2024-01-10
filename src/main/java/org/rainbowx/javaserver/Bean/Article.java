package org.rainbowx.javaserver.Bean;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int aid;
    @ManyToOne
    @JoinColumn(name = "author", referencedColumnName = "uid")
    private User author;
    @Column(length = 50)
    private String title;
    private LocalDateTime time;
    @Column(columnDefinition = "TEXT")
    private String content;

    @SuppressWarnings("unused")
    protected Article(int aid, User author, String title, LocalDateTime time, String content) {
        this.aid = aid;
        this.author = author;
        this.title = title;
        this.time = time;
        this.content = content;
    }

    public Article() {

    }

    public int getAid() {
        return aid;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
