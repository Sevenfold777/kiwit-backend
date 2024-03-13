package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.UserInfoDAO;
import com.kiwit.backend.domain.UserInfo;
import com.kiwit.backend.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        return savedUserInfo.get();
    }

    @Override
    public UserInfo insertUserInfo(UserInfo userInfo) {
        UserInfo savedUserInfo = userInfoRepository.save(userInfo);
        return savedUserInfo;
    }

    @Override
    public UserInfo updateUserInfo(UserInfo userInfo) {
        UserInfo savedUserInfo = userInfoRepository.save(userInfo);
        return savedUserInfo;
    }

    @Override
    public UserInfo updateRefreshToken(UserInfo userInfo) {
        UserInfo savedUserInfo = userInfoRepository.save(userInfo);
        return savedUserInfo;
    }
}
