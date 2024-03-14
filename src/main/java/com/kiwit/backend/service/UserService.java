package com.kiwit.backend.service;

import com.kiwit.backend.domain.User;
import com.kiwit.backend.dto.*;

import java.util.List;

public interface UserService {

    SignInResDTO signUp(SignUpReqDTO signUpReqDTO);
    SignInResDTO signIn(SignInReqDTO signInReqDTO);
    void signOut(User user);
    SignInResDTO refreshToken(RefreshTokenDTO refreshTokenDTO);
    UserDTO myInfo(User user);
    UserDTO editUser(User user, EditUserReqDTO userDTO);
    void withdrawUser(User user);
    List<TrophyAwardedDTO> getMyTrophyList(User user);
    TrophyAwardedDTO getMyTrophyLatest(User user);
    StatDTO getMyStat(User user);
}
