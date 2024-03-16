package com.kiwit.backend.dao.impl;

import com.kiwit.backend.common.exception.CustomException;
import com.kiwit.backend.dao.UserInfoDAO;
import com.kiwit.backend.domain.UserInfo;
import com.kiwit.backend.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        Optional<UserInfo> savedUserInfo = userInfoRepository.findById(userId);
        if (savedUserInfo.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        return savedUserInfo.get();
    }

    @Override
    public UserInfo insertUserInfo(UserInfo userInfo) {
        try {
            return userInfoRepository.save(userInfo);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public UserInfo updateUserInfo(UserInfo userInfo) {
        try {
            return userInfoRepository.save(userInfo);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public UserInfo updateRefreshToken(UserInfo userInfo) {
        try {
            return userInfoRepository.save(userInfo);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
    }
}
