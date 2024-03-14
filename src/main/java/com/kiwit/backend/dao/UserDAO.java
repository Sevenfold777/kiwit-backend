package com.kiwit.backend.dao;

import com.kiwit.backend.domain.User;
import com.kiwit.backend.domain.UserInfo;

public interface UserDAO {

    User insertUser(User user);
    User selectUser(Long userId);
    User selectUserWithInfo(Long userId);

}
