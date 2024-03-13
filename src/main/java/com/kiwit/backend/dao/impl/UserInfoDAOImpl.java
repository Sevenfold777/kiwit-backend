package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.UserInfoDAO;
import com.kiwit.backend.domain.UserInfo;
import com.kiwit.backend.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserInfoDAOImpl implements UserInfoDAO {

    private final UserInfoRepository userInfoRepository;

    @Autowired
    public UserInfoDAOImpl(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public UserInfo insertUserInfo(UserInfo userInfo) {
        return null;
    }

    @Override
    public UserInfo updateUserInfo(UserInfo userInfo) {
        return null;
    }

    @Override
    public UserInfo updateRefreshToken(Long userId, String refreshToken) {
        return null;
    }
}
