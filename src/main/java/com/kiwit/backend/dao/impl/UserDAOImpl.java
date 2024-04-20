package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.UserDAO;
import com.kiwit.backend.domain.User;
import com.kiwit.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Component
public class UserDAOImpl implements UserDAO {

    private final UserRepository userRepository;

    @Autowired
    public UserDAOImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User insertUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User selectUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DataAccessException("Cannot find user with id.") {});
    }

    @Override
    public User selectUserWithInfo(Long userId) {
        return userRepository.findUserWithInfo(userId)
                .orElseThrow(() -> new DataAccessException("Cannot find user with id.") {});
    }

    @Override
    public User selectUserWithEmail(String email) {
        return userRepository.findUserWithInfoByEmail(email)
                .orElseThrow(() -> new DataAccessException("Cannot find user with email") {});
    }

    @Override
    public User getUserProxy(Long userId) {
        return userRepository.getOne(userId);
    }
}
