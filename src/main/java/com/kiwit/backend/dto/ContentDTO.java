package com.kiwit.backend.dto;

import com.kiwit.backend.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class ContentDTO {

    private Long id;
    private String title;
    private Integer point;
    private String exercise;
    private Boolean answer;
    private Long levelNum;
    private Long categoryChapterId;
    private String payloadUrl;
}
