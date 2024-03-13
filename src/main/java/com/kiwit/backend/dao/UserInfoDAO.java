package com.kiwit.backend.dao;

import com.kiwit.backend.domain.UserInfo;

public interface UserInfoDAO {

    UserInfo findUserInfo(Long userId);
    UserInfo insertUserInfo(UserInfo userInfo);
    UserInfo updateUserInfo(UserInfo userInfo);
    UserInfo updateRefreshToken(UserInfo userInfo);


}
