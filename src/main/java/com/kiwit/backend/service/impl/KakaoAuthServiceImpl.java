package com.kiwit.backend.service.impl;

import com.kiwit.backend.common.constant.Provider;
import com.kiwit.backend.common.exception.CustomException;
import com.kiwit.backend.dto.SignUpReqDTO;
import com.kiwit.backend.dto.kakao.KakaoUserInfoResponse;
import com.kiwit.backend.service.KakaoAuthService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KakaoAuthServiceImpl implements KakaoAuthService {
    @Override
    public SignUpReqDTO getUserProfile(String token) {

        String url = "https://kapi.kakao.com/v2/user/me";

        WebClient webClient = WebClient.create();

        WebClient.ResponseSpec responseSpec = webClient
                .get()
                .uri(url)
                .header("Authorization", "Bearer " + token)
                .retrieve();

        try {
            KakaoUserInfoResponse response = responseSpec
                    .bodyToMono(KakaoUserInfoResponse.class)
                    .block();

            SignUpReqDTO authResultDTO = SignUpReqDTO
                    .builder()
                    .email(response.getKakao_account().getEmail())
                    .nickname(response.getKakao_account().getProfile().getNickname())
                    .provider(Provider.KAKAO)
                    .build();

            return authResultDTO;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }

    }
}
