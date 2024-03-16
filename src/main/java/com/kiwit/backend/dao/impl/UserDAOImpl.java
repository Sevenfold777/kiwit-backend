package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.UserDAO;
import com.kiwit.backend.domain.User;
import com.kiwit.backend.domain.UserInfo;
import com.kiwit.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDAOImpl implements UserDAO {

    private UserRepository userRepository;

    @Autowired
    public UserDAOImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User insertUser(User user) {
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    @Override
    public User selectUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.get();
    }

    @Override
    public User selectUserWithInfo(Long userId) {
        Optional<User> user = userRepository.findUserWithInfo(userId);
        return user.get();
    }

    @Override
    public User selectUserWithEmail(String email) {
        Optional<User> user = userRepository.findUserWithInfoByEmail(email);
        return user.get();
    }

}
