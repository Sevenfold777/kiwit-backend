package com.kiwit.backend.dao.impl;

import com.kiwit.backend.common.exception.CustomException;
import com.kiwit.backend.dao.UserInfoDAO;
import com.kiwit.backend.domain.UserInfo;
import com.kiwit.backend.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInfoDAOImpl implements UserInfoDAO {

    private final UserInfoRepository userInfoRepository;

    @Autowired
    public UserInfoDAOImpl(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public UserInfo findUserInfo(Long userId) {
        return userInfoRepository.findById(userId)
                .orElseThrow(() -> new DataAccessException("Cannot find User Info by userId.") {});
    }

    @Override
    public UserInfo saveUserInfo(UserInfo userInfo) {
        return userInfoRepository.save(userInfo);
    }

    @Override
    public UserInfo updateUserInfo(UserInfo userInfo) {
        return userInfoRepository.save(userInfo);
    }

    @Override
    public UserInfo updateRefreshToken(UserInfo userInfo) {
        return userInfoRepository.save(userInfo);
    }
}
