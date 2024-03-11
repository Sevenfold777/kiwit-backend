package com.kiwit.backend.service;

import com.kiwit.backend.dto.*;

public interface UserService {

    SignInResDTO signUp(SignUpReqDTO signUpReqDTO);
    SignInResDTO signIn(SignInReqDTO signInReqDTO);
    void signOut();
    SignInResDTO refreshToken(RefreshTokenDTO refreshTokenDTO);
    UserDTO myInfo();
    UserDTO editUser(UserDTO userDTO);
    void withdrawUser();
    TrophyDto getMyTrophyList();
    TrophyDto getMyTrophyLatest();
    StatDto getMyStat();
}
