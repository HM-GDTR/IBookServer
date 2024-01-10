package org.rainbowx.javaserver.Service;

import org.rainbowx.javaserver.Bean.User;
import org.rainbowx.javaserver.DAO.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserService {
    private static final String TAG = "UserService";
    protected static final Logger logger;
    protected UserRepository userRepository;

    static {
        logger = Logger.getLogger(TAG);
    }

    @Autowired
    public UserService(UserRepository repository) {
        this.userRepository = repository;
    }

    /**
     * 注册用户
     * @param username 用户名
     * @param passwdSha256 密码加盐后的哈希值
     * @param passSalt 密码的盐
     * @return 如果成功, 返回uid, 否则返回-1
     */
    public int signup(String username, String passwdSha256, String passSalt) {
        try {
            User user = new User();
            user.setUserName(username);
            user.setPassHashSha256(passwdSha256);
            user.setPassSalt(passSalt);
            user.setRegistrationDate(LocalDateTime.now());
            user = userRepository.save(user);
            return user.getUid();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE,"Error: ",e);
            return -1;
        }
    }

    /**
     * 修改用户id为uid的密码
     * @param uid 用户id
     * @param passwdSha256 新密码加盐后的哈希值
     * @param passSalt 新密码的盐
     * @return 如果成功, 返回0, 否则返回非零值
     */
    public int modifyPassWd(int uid, String passwdSha256, String passSalt) {
        try {
            User user = userRepository.findUserByUid(uid);
            user.setPassSalt(passSalt);
            user.setPassHashSha256(passwdSha256);
            user = userRepository.save(user);
            if(!user.getPassSalt().equals(passwdSha256)) throw new RuntimeException("密码盐未更改");
            if(!user.getPassHashSha256().equals(passwdSha256)) throw new RuntimeException("密码哈希未更改");
        }
        catch (Exception e) {
            logger.log(Level.SEVERE,"Error: ",e);
            return -1;
        }
        return 0;
    }

    /**
     * 修改个人信息
     * @param uid 需要修改的用户的id
     * @param avatar 新的头像(如果不需要修改就填null, 下同)
     * @param nickName 新的昵称
     * @param sign 新的个性签名
     * @param birthday 新的生日
     * @param gender 新的性别(需要这个是因为新建用户的时候为null)
     * @return 成功返回0, 失败返回-1
     */
    public int modify(int uid, String avatar, String nickName, String sign, LocalDateTime birthday, String gender) {
        try {
            User user = userRepository.findUserByUid(uid);

            if(avatar != null) user.setAvatar(avatar);
            if(nickName != null) user.setNickName(nickName);
            if(sign != null) user.setSign(sign);
            if(birthday != null) user.setBirthday(birthday);
            if(gender != null) user.setGender(gender);

            userRepository.save(user);

            return 0;
        }
        catch (Exception e) {
            logger.log(Level.SEVERE,"Error: ",e);
            return -1;
        }
    }

    /**
     * 使用uid为关键字寻找用户
     * @param uid 用户id
     * @return 如果成功返回用户, 否则返回null
     */
    public User findUserByUid(int uid) {
        try {
            return userRepository.findUserByUid(uid);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE,"Error: ",e);
            return null;
        }
    }

    /**
     * 使用用户名为关键字寻找用户
     * @param userName 用户名
     * @return 如果成功返回用户, 否则返回null
     */
    public User findUserByUserName(String userName) {
        try {
            return userRepository.findUserByUserName(userName);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE,"Error: ",e);
            return null;
        }
    }
}
