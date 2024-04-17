package com.kiwit.backend.service.impl;

import com.kiwit.backend.common.constant.Provider;
import com.kiwit.backend.config.security.JwtTokenProvider;
import com.kiwit.backend.domain.User;
import com.kiwit.backend.dto.SignInResDTO;
import com.kiwit.backend.dto.SignUpReqDTO;
import com.kiwit.backend.dto.UserDTO;
import com.kiwit.backend.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("UserService Test")
class UserServiceImplTest {

    @Autowired UserService userService;
    @Autowired JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("회원가입 성공")
    public void signUpSuccessTest() throws Exception {

        // Given
        String email = "spring@gmail.com";
        String nickname = "signUpTest";
        SignUpReqDTO signUpReqDTO = new SignUpReqDTO(email, nickname, Provider.KAKAO);

        // When
        SignInResDTO signInResDTO = userService.signUp(signUpReqDTO); // sign-up
        Long userId = jwtTokenProvider.getUserId(signInResDTO.getAccessToken()); // get userId from jwt

        // Then
        UserDTO userDTO = userService.myInfo(new User(userId)); // myInfo
        assertEquals(email, userDTO.getEmail());
    }


}