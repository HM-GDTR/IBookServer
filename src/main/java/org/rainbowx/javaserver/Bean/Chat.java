package org.rainbowx.javaserver.Bean;

import jakarta.persistence.*;

@Entity
@Table(schema = "chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cid;
    @ManyToOne
    @JoinColumn(name = "source", referencedColumnName = "uid")
    private User source;
    @ManyToOne
    @JoinColumn(name = "dest", referencedColumnName = "uid")
    private User dest;
    @Column(columnDefinition = "TEXT")
    private String content;

    protected Chat(int cid, User source, User dest, String content) {
        this.cid = cid;
        this.source = source;
        this.dest = dest;
        this.content = content;
    }

    public Chat() {
    }

    public int getCid() {
        return cid;
    }

    public User getSource() {
        return source;
    }

    public void setSource(User source) {
        this.source = source;
    }

    public User getDest() {
        return dest;
    }

    public void setDest(User dest) {
        this.dest = dest;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
