package com.kiwit.backend.service.impl;

import com.kiwit.backend.config.security.JwtTokenProvider;
import com.kiwit.backend.dao.ProgressDAO;
import com.kiwit.backend.dao.UserDAO;
import com.kiwit.backend.dao.UserInfoDAO;
import com.kiwit.backend.domain.*;
import com.kiwit.backend.dto.*;
import com.kiwit.backend.repository.UserRepository;
import com.kiwit.backend.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    UserDAO userDAO;
    UserInfoDAO userInfoDAO;
    ProgressDAO progressDAO;
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserServiceImpl(UserDAO userDAO,
                           UserInfoDAO userInfoDAO,
                           ProgressDAO progressDAO,
                           JwtTokenProvider jwtTokenProvider) {
        this.userDAO = userDAO;
        this.userInfoDAO = userInfoDAO;
        this.progressDAO = progressDAO;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public SignInResDTO signUp(SignUpReqDTO signUpReqDTO) {

        // set user with request body
        User user = new User(signUpReqDTO.getEmail(), signUpReqDTO.getNickname());

        // set progress with existing content id: 1 (initial content)
        Progress progress = new Progress(user);
        progress.setContent(new Content(1L));

        // set user info
        UserInfo userInfo = UserInfo
                .builder()
                .user(user)
                .latestVisit(LocalDateTime.now())
                .build();

        UserInfo savedUserInfo = userInfoDAO.insertUserInfo(userInfo);
        Progress savedProgress = progressDAO.insertProgress(progress);

        // issue token with id
        String accessToken = jwtTokenProvider.issueToken(savedUserInfo.getId(), false);
        String refreshToken = jwtTokenProvider.issueToken(savedUserInfo.getId(), true);

        SignInResDTO signInResDTO = new SignInResDTO(accessToken, refreshToken);

        // save jwt refresh token in UserInfo (for JWT Refresh Token Rotation)
        savedUserInfo.setJwtRefreshToken(refreshToken);
        userInfoDAO.updateRefreshToken(savedUserInfo);

        return signInResDTO;
    }

    @Transactional
    @Override
    public SignInResDTO signIn(SignInReqDTO signInReqDTO) {

        // 1. deal with third party auth token
        // ...

        // 2. sign JWT access token, refresh token
        // for test - sign in with id: 1
        Long userId = 1L;

        String accessToken = jwtTokenProvider.issueToken(userId, false);
        String refreshToken = jwtTokenProvider.issueToken(userId, true);

        // dirty checking
        UserInfo userInfo = userInfoDAO.findUserInfo(userId);
        userInfo.setJwtRefreshToken(refreshToken);

        SignInResDTO signInResDTO = new SignInResDTO(accessToken, refreshToken);

        return signInResDTO;
    }

    @Transactional
    @Override
    public void signOut() {
        Long userId = 1L;

        // dirty checking
        UserInfo userInfo = userInfoDAO.findUserInfo(userId);
        userInfo.setJwtRefreshToken(null);
        return;
    }


    @Transactional
    @Override
    public SignInResDTO refreshToken(RefreshTokenDTO refreshTokenDTO) {
         Long userId = jwtTokenProvider.getUserId(refreshTokenDTO.getRefreshToken());

        String accessToken = jwtTokenProvider.issueToken(userId, false);
        String refreshToken = jwtTokenProvider.issueToken(userId, true);

        // dirty checking
        UserInfo userInfo = userInfoDAO.findUserInfo(userId);

        // Check if refresh token is valid
        if (!userInfo.getJwtRefreshToken().equals(refreshTokenDTO.getRefreshToken())) {
            // TODO: throw error
            System.out.println("Error!");
            return null;
        }

        userInfo.setJwtRefreshToken(refreshToken);

        SignInResDTO signInResDTO = new SignInResDTO(accessToken, refreshToken);
        return signInResDTO;
    }

    @Override
    public UserDTO myInfo() {
        UserDTO userDTO = new UserDTO();
        return userDTO;
    }

    @Override
    public UserDTO editUser(UserDTO userDTO) {
        UserDTO resDTO = new UserDTO();
        return resDTO;
    }

    @Override
    public void withdrawUser() {
        return;
    }

    @Override
    public TrophyDTO getMyTrophyList() {
        TrophyDTO trophyDto = new TrophyDTO();
        return trophyDto;
    }

    @Override
    public TrophyDTO getMyTrophyLatest() {
        TrophyDTO trophyDto = new TrophyDTO();
        return trophyDto;
    }

    @Override
    public StatDTO getMyStat() {
        StatDTO statDto = new StatDTO();
        return statDto;
    }
}
