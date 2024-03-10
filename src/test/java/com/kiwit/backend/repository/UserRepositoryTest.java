package com.kiwit.backend.repository;

import com.kiwit.backend.domain.User;
import com.kiwit.backend.domain.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Test
    public void createUserTest() {

        User user = new User();
        user.setNickname("jaewon");
        user.setEmail("jwonp98@naver.com");

//        user.setPoint(0);
//        user.setCurrent_level(0);


        UserInfo userInfo = new UserInfo();
        userInfo.setUser(user);
        userInfo.setFcmToken("123asd");
        userInfo.setLatestVisit(LocalDateTime.now());
        userInfo.setJwtRefreshToken("456fgh");
        userInfoRepository.save(userInfo);

        System.out.println("saved user : " + userRepository.findById(
                user.getId()
        ).get());

        System.out.println("user.userinfo : " + userRepository.findById(
                user.getId()
        ).get().getUserInfo());

        System.out.println("userinfo : " + userInfoRepository.findById(
                user.getId()
        ).get());
    }
}
