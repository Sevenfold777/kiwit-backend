package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.UserInfoDAO;
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

}
