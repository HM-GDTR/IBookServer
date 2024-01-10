package org.rainbowx.javaserver.Service;

import org.rainbowx.javaserver.Bean.Chat;
import org.rainbowx.javaserver.Bean.User;
import org.rainbowx.javaserver.DAO.ChatRepository;
import org.rainbowx.javaserver.DAO.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ChatService {
    private static final String TAG = "ChatService";
    protected static Logger logger;
    protected ChatRepository chatRepository;
    protected UserRepository userRepository;

    static {
        logger = Logger.getLogger(TAG);
    }

    @Autowired
    public ChatService(ChatRepository repository, UserRepository repository1) {
        this.chatRepository = repository;
        this.userRepository = repository1;
    }

    /**
     * 添加一条消息
     * @param source 消息来源
     * @param dest 消息目标
     * @param content 消息内容
     * @return 如果成功返回cid, 否则返回-1
     */
    public int addChat(int source, int dest, String content){
        try {
            User origin, target;
            Chat chat = new Chat();

            target = userRepository.findUserByUid(dest);
            origin = userRepository.findUserByUid(source);

            if(origin == null || target == null) throw new RuntimeException("UID错误");

            chat.setDest(target);
            chat.setSource(origin);
            chat.setContent(content);
            chat = chatRepository.save(chat);
            return chat.getCid();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE,"Error: ",e);
            return -1;
        }
    }

    /**
     * 通过双方的uid寻找聊天记录(注意, 双方不分发起方和接收方, 最后根据时间排序)
     * @param lhs 聊天一方的uid
     * @param rhs 聊天另一方的uid
     * @return 如果成功返回聊天的列表, 否则返回null
     */
    public List<Chat> queryChats(int lhs, int rhs) {
        try {
            User lh,rh;
            lh = userRepository.findUserByUid(lhs);
            rh = userRepository.findUserByUid(rhs);
            if(lh == null || rh == null) throw new RuntimeException("UID错误");

            List<Chat> ret = new ArrayList<>();
            List<Chat> lList = chatRepository.findChatsBySourceAndDest(lh, rh);
            List<Chat> rList = chatRepository.findChatsBySourceAndDest(rh, lh);
            ret.addAll(lList);
            ret.addAll(rList);
            ret.sort(new Comparator<Chat>() {
                @Override
                public int compare(Chat lh, Chat rh) {
                    return Integer.compare(lh.getCid(), rh.getCid());
                }
            });
            return ret;
        }
        catch (Exception e) {
            logger.log(Level.SEVERE,"Error: ",e);
            return null;
        }
    }

    /**
     * 根据聊天双方的某一方进行搜索
     * @param other 某一方的uid
     * @return 如果成功返回聊天的列表, 否则返回null
     */
    public List<Chat> queryChats(int other) {
        try {
            User side = userRepository.findUserByUid(other);
            if(side == null) throw new RuntimeException("UID错误");
            return chatRepository.findChatsBySourceOrDest(side, side);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE,"Error: ",e);
            return null;
        }
    }
}
