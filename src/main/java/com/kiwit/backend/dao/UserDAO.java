package com.kiwit.backend.dao;

import com.kiwit.backend.domain.User;
import com.kiwit.backend.domain.UserInfo;

public interface UserDAO {

    User insertUser(User user);
    User updateUser(User user);
    User updateNickname(Long userId, String nickname);
    User selectUser(Long userId);

}
