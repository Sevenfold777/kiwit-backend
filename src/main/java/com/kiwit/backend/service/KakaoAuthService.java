package com.kiwit.backend.service;

import com.kiwit.backend.dto.SignUpReqDTO;

public interface KakaoAuthService {

    SignUpReqDTO getUserProfile(String token);
}
