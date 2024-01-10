package org.rainbowx.javaserver.DAO;

import org.rainbowx.javaserver.Bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByUid(int uid);
    User findUserByUserName(String userName);
}
