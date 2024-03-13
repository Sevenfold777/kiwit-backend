package com.kiwit.backend.dao;

import com.kiwit.backend.domain.UserInfo;

public interface UserInfoDAO {

    UserInfo insertUserInfo(UserInfo userInfo);
    UserInfo updateUserInfo(UserInfo userInfo);
    UserInfo updateRefreshToken(Long userId, String refreshToken);


}
