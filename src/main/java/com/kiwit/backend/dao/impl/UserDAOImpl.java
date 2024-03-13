package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.UserDAO;
import com.kiwit.backend.domain.User;
import com.kiwit.backend.domain.UserInfo;
import com.kiwit.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDAOImpl implements UserDAO {

    private UserRepository userRepository;

    @Autowired
    public UserDAOImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User insertUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public User selectUser(Long userId) {
        return null;
    }


    @Override
    public User updateNickname(Long userId, String nickname) {
        return null;
    }

}
