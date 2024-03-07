package com.kiwit.backend.service.impl;

import com.kiwit.backend.repository.UserRepository;
import com.kiwit.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void signUp() {
        return;
    }
    @Override
    public void signIn() {
        return;
    }
    @Override
    public void refreshToken() {
        return;
    }
    @Override
    public void myInfo() {
        return;
    }
    @Override
    public void editUser() {
        return;
    }
    @Override
    public void withdrawUser() {
        return;
    }
}
