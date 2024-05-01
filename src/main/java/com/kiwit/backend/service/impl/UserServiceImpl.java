package com.kiwit.backend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiwit.backend.common.constant.Provider;
import com.kiwit.backend.common.constant.Status;
import com.kiwit.backend.common.exception.CustomException;
import com.kiwit.backend.config.security.JwtTokenProvider;
import com.kiwit.backend.dao.TrophyAwardedDAO;
import com.kiwit.backend.dao.UserDAO;
import com.kiwit.backend.dao.UserInfoDAO;
import com.kiwit.backend.domain.*;
import com.kiwit.backend.dto.*;
import com.kiwit.backend.service.KakaoAuthService;
import com.kiwit.backend.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final UserInfoDAO userInfoDAO;
    private final TrophyAwardedDAO trophyAwardedDAO;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoAuthService kakaoAuthService;

    @Autowired
    public UserServiceImpl(UserDAO userDAO,
                           UserInfoDAO userInfoDAO,
                           TrophyAwardedDAO trophyAwardedDAO,
                           JwtTokenProvider jwtTokenProvider,
                           KakaoAuthService kakaoAuthService) {
        this.userDAO = userDAO;
        this.userInfoDAO = userInfoDAO;
        this.trophyAwardedDAO = trophyAwardedDAO;
        this.jwtTokenProvider = jwtTokenProvider;
        this.kakaoAuthService = kakaoAuthService;
    }

    @Transactional
    @Override
    public SignInResDTO signUp(SignUpReqDTO signUpReqDTO) {

        // set user with request body
        User user = new User(signUpReqDTO.getEmail(), signUpReqDTO.getNickname());

        // set user info
        UserInfo userInfo = UserInfo
                .builder()
                .user(user)
                .provider(signUpReqDTO.getProvider())
                .build();

        UserInfo savedUserInfo = userInfoDAO.saveUserInfo(userInfo);

        // issue token with id
        String accessToken = jwtTokenProvider.issueToken(savedUserInfo.getId(), false);
        String refreshToken = jwtTokenProvider.issueToken(savedUserInfo.getId(), true);

        SignInResDTO signInResDTO = new SignInResDTO(accessToken, refreshToken);

        // save jwt refresh token in UserInfo (for JWT Refresh Token Rotation)
        savedUserInfo.setJwtRefreshToken(refreshToken);

        return signInResDTO;
    }

    @Transactional
    @Override
    public SignInResDTO signIn(SignInReqDTO signInReqDTO) {

        // 1. deal with third party auth token
        SignUpReqDTO authResult;
        switch (signInReqDTO.getProvider()) {
            case KAKAO -> {
                authResult = kakaoAuthService.getUserProfile(signInReqDTO.getToken());
            }
            // case APPLE 추가 예정
            default -> {
                throw new CustomException(HttpStatus.BAD_REQUEST);
            }
        }

        try {
            // 2-1. sign JWT access token, refresh token
            User user = userDAO.selectUserWithEmail(authResult.getEmail());

            String accessToken = jwtTokenProvider.issueToken(user.getId(), false);
            String refreshToken = jwtTokenProvider.issueToken(user.getId(), true);

            // dirty checking
            user.setStatus(Status.ACTIVATED); // deactivated user to activated
            user.getUserInfo().setJwtRefreshToken(refreshToken); // refresh token rotation

            SignInResDTO signInResDTO = new SignInResDTO(accessToken, refreshToken);

            return signInResDTO;
        } catch (Exception e) {
            // 2-2. cannot find user with email; redirect to sign up
            SignUpReqDTO signUpReqDTO = SignUpReqDTO
                    .builder()
                    .email(authResult.getEmail())
                    .nickname(authResult.getNickname())
                    .provider(authResult.getProvider())
                    .build();

            ObjectMapper objectMapper = new ObjectMapper();

            throw new CustomException(HttpStatus.ACCEPTED, objectMapper.convertValue(signUpReqDTO, Map.class));
        }
    }

    @Transactional
    @Override
    public void signOut(User user) {
        // dirty checking
        UserInfo userInfo = userInfoDAO.findUserInfo(user.getId());
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
            throw new CustomException(HttpStatus.UNAUTHORIZED);
        }

        userInfo.setJwtRefreshToken(refreshToken);

        SignInResDTO signInResDTO = new SignInResDTO(accessToken, refreshToken);
        return signInResDTO;
    }

    @Override
    public UserDTO myProfile(User user) {

        User me = userDAO.selectUser(user.getId());

        UserDTO userDTO = UserDTO
                .builder()
                .id(me.getId())
                .email(me.getEmail())
                .nickname(me.getNickname())
                .plan(me.getPlan())
                .status(me.getStatus())
                .point(me.getPoint())
                .build();

        return userDTO;
    }

    @Transactional
    @Override
    public UserDTO editUser(User user, EditUserReqDTO userDTO) {
        try {
            User me = userDAO.selectUser(user.getId());
            me.setNickname(userDTO.getNickname());

            UserDTO userResDTO = UserDTO
                    .builder()
                    .id(me.getId())
                    .email(me.getEmail())
                    .nickname(me.getNickname())
                    .plan(me.getPlan())
                    .status(me.getStatus())
                    .point(me.getPoint())
                    .build();

            return userResDTO;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    @Override
    public void withdrawUser(User user) {
        User me = userDAO.selectUserWithInfo(user.getId());

        me.setStatus(Status.DEACTIVATED);
        me.getUserInfo().setJwtRefreshToken(null);
    }

    @Override
    public List<TrophyAwardedDTO> getMyTrophyList(User user, Integer next, Integer limit) {

        List<TrophyAwarded> trophyAwardedList
                = trophyAwardedDAO.selectMyTrophyAwarded(user.getId(), next, limit);

        List<TrophyAwardedDTO> trophyAwardedDTOList = new ArrayList<>();

        for (TrophyAwarded ta : trophyAwardedList) {

            Trophy trophy = ta.getTrophy();

            TrophyDTO trophyDTO = TrophyDTO
                    .builder()
                    .id(trophy.getId())
                    .title(trophy.getTitle())
                    .imageUrl(trophy.getImageUrl())
                    .build();

            TrophyAwardedDTO trophyAwardedDTO
                    = TrophyAwardedDTO
                    .builder()
                    .userId(ta.getId().getUserId())
                    .trophy(trophyDTO)
                    .createdAt(ta.getCreatedAt())
                    .updatedAt(ta.getUpdatedAt())
                    .build();

            trophyAwardedDTOList.add(trophyAwardedDTO);
        }


        return trophyAwardedDTOList;
    }

    @Override
    public TrophyAwardedDTO getMyTrophyLatest(User user) {

        TrophyAwarded trophyAwarded
                = trophyAwardedDAO.selectTrophyAwardedLatest(user.getId());

        Trophy trophy = trophyAwarded.getTrophy();

        TrophyDTO trophyDTO = TrophyDTO
                .builder()
                .id(trophy.getId())
                .title(trophy.getTitle())
                .imageUrl(trophy.getImageUrl())
                .build();

        TrophyAwardedDTO trophyAwardedDTO = TrophyAwardedDTO
                .builder()
                .userId(trophyAwarded.getId().getUserId())
                .trophy(trophyDTO)
                .createdAt(trophyAwarded.getCreatedAt())
                .updatedAt(trophyAwarded.getUpdatedAt())
                .build();

        return trophyAwardedDTO;
    }

    @Override
    public StatDTO getMyStat(User user) {
        StatDTO statDto = new StatDTO();
        return statDto;
    }
}
