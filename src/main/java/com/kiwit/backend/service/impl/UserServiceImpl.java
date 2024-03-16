package com.kiwit.backend.service.impl;

import com.kiwit.backend.common.constant.Provider;
import com.kiwit.backend.common.constant.Status;
import com.kiwit.backend.config.security.JwtTokenProvider;
import com.kiwit.backend.dao.ProgressDAO;
import com.kiwit.backend.dao.TrophyAwardedDAO;
import com.kiwit.backend.dao.UserDAO;
import com.kiwit.backend.dao.UserInfoDAO;
import com.kiwit.backend.domain.*;
import com.kiwit.backend.dto.*;
import com.kiwit.backend.repository.UserRepository;
import com.kiwit.backend.service.KakaoAuthService;
import com.kiwit.backend.service.UserService;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    UserDAO userDAO;
    UserInfoDAO userInfoDAO;
    ProgressDAO progressDAO;
    TrophyAwardedDAO trophyAwardedDAO;
    JwtTokenProvider jwtTokenProvider;
    KakaoAuthService kakaoAuthService;

    @Autowired
    public UserServiceImpl(UserDAO userDAO,
                           UserInfoDAO userInfoDAO,
                           ProgressDAO progressDAO,
                           TrophyAwardedDAO trophyAwardedDAO,
                           JwtTokenProvider jwtTokenProvider,
                           KakaoAuthService kakaoAuthService) {
        this.userDAO = userDAO;
        this.userInfoDAO = userInfoDAO;
        this.progressDAO = progressDAO;
        this.trophyAwardedDAO = trophyAwardedDAO;
        this.jwtTokenProvider = jwtTokenProvider;
        this.kakaoAuthService = kakaoAuthService;
    }

    @Transactional
    @Override
    public SignInResDTO signUp(SignUpReqDTO signUpReqDTO) {
        Long INIT_CONTENT = 1L;

        // set user with request body
        User user = new User(signUpReqDTO.getEmail(), signUpReqDTO.getNickname());

        // set progress with existing content id: 1 (initial content)
        Progress progress = new Progress(user);
        progress.setContent(new Content(INIT_CONTENT));

        // set user info
        UserInfo userInfo = UserInfo
                .builder()
                .user(user)
                .provider(signUpReqDTO.getProvider())
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
        SignUpReqDTO authResult;
        switch (signInReqDTO.getProvider()) {
            case KAKAO -> {
                authResult = kakaoAuthService.getUserProfile(signInReqDTO.getToken());
            }
            default -> {
                authResult = SignUpReqDTO
                        .builder()
                        .email("oopoop@oop.com")
                        .nickname("ggg")
                        .provider(Provider.KAKAO)
                        .build();
                // throw error
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

//            throw new BadRequestException(signUpReqDTO);
            return null;
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
            // TODO: throw error
            System.out.println("Error!");
            return null;
        }

        userInfo.setJwtRefreshToken(refreshToken);

        SignInResDTO signInResDTO = new SignInResDTO(accessToken, refreshToken);
        return signInResDTO;
    }

    @Override
    public UserDTO myInfo(User user) {

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
    }

    @Transactional
    @Override
    public void withdrawUser(User user) {
        User me = userDAO.selectUserWithInfo(user.getId());

        // TODO: enum
        me.setStatus(Status.DEACTIVATED);
        me.getUserInfo().setJwtRefreshToken(null);
        return;
    }

    @Override
    public List<TrophyAwardedDTO> getMyTrophyList(User user) {

        List<TrophyAwarded> trophyAwardedList
                = trophyAwardedDAO.selectMyTrophyAwarded(user.getId());

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
