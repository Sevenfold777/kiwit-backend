package com.kiwit.backend.service.impl;

import com.kiwit.backend.dao.UserDAO;
import com.kiwit.backend.domain.Trophy;
import com.kiwit.backend.dto.*;
import com.kiwit.backend.repository.UserRepository;
import com.kiwit.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    UserDAO userDAO;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public SignInResDTO signUp(SignUpReqDTO signUpReqDTO) {

        SignInResDTO signInResDTO = new SignInResDTO();
        signInResDTO.setAccessToken("asd");
        signInResDTO.setRefreshToken("zxc");
        return signInResDTO;
    }

    @Override
    public SignInResDTO signIn(SignInReqDTO signInReqDTO) {

        SignInResDTO signInResDTO = new SignInResDTO();
        return signInResDTO;
    }

    @Override
    public SignInResDTO refreshToken(RefreshTokenDTO refreshTokenDTO) {
        SignInResDTO signInResDTO = new SignInResDTO("123", "zxc");
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
    public void signOut() {
        return;
    }

    @Override
    public TrophyDto getMyTrophyList() {
        TrophyDto trophyDto = new TrophyDto();
        return trophyDto;
    }

    @Override
    public TrophyDto getMyTrophyLatest() {
        TrophyDto trophyDto = new TrophyDto();
        return trophyDto;
    }

    @Override
    public StatDto getMyStat() {
        StatDto statDto = new StatDto();
        return statDto;
    }
}
