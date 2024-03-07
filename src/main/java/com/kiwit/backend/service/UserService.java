package com.kiwit.backend.service;

public interface UserService {

    void signUp();
    void signIn();
    void refreshToken();
    void myInfo();
    void editUser();
    void withdrawUser();
}
