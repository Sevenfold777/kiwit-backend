package com.kiwit.backend.dto.kakao;

import lombok.Getter;

@Getter
public class KakaoAccount {

    private Boolean profile_nickname_needs_agreement;
    private Boolean profile_image_needs_agreement;
    private KakaoProfile profile;

    private String email;
    private Boolean has_email;
    private Boolean email_needs_agreement;
    private Boolean is_email_valid;
    private Boolean is_email_verified;
}
