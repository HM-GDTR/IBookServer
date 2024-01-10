package org.rainbowx.javaserver.DAO;

import org.rainbowx.javaserver.Bean.Chat;
import org.rainbowx.javaserver.Bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
    @SuppressWarnings("unused")
    Chat findChatByCid(int cid);
    List<Chat> findChatsBySourceAndDest(User source, User dest);
    List<Chat> findChatsBySourceOrDest(User source, User dest);
}
