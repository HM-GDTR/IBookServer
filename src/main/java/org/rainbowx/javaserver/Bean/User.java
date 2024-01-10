package org.rainbowx.javaserver.Bean;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(schema = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uid;
    @Column(length = 50, unique = true)
    private String userName;
    @Column(length = 64)
    private String passHashSha256;
    @Column(length = 50)
    private String passSalt;

    @Column(columnDefinition = "TEXT")
    private String avatar;
    @Column(length = 50)
    private String nickName;
    @Column(length = 255)
    private String sign;
    private LocalDateTime birthday;
    @Column(length = 50)
    private String gender;
    private LocalDateTime registrationDate;

    protected User(int uid, String userName, String passHashSha256, String passSalt, String avatar, String nickName, String sign, LocalDateTime birthday, String gender, LocalDateTime registrationDate) {
        this.uid = uid;
        this.userName = userName;
        this.passHashSha256 = passHashSha256;
        this.passSalt = passSalt;
        this.avatar = avatar;
        this.nickName = nickName;
        this.sign = sign;
        this.birthday = birthday;
        this.gender = gender;
        this.registrationDate = registrationDate;
    }

    public User() {

    }

    public int getUid() {
        return uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassHashSha256() {
        return passHashSha256;
    }

    public void setPassHashSha256(String passHashSha256) {
        this.passHashSha256 = passHashSha256;
    }

    public String getPassSalt() {
        return passSalt;
    }

    public void setPassSalt(String passSalt) {
        this.passSalt = passSalt;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }
}
