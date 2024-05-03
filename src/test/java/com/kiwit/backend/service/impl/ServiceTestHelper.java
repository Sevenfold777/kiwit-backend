package com.kiwit.backend.service.impl;

import com.kiwit.backend.common.constant.ContentType;
import com.kiwit.backend.common.constant.Provider;
import com.kiwit.backend.common.constant.QuizType;
import com.kiwit.backend.config.security.JwtTokenProvider;
import com.kiwit.backend.domain.*;
import com.kiwit.backend.domain.compositeKey.ContentPayloadId;
import com.kiwit.backend.domain.compositeKey.QuizChoiceId;
import com.kiwit.backend.domain.compositeKey.TrophyAwardedId;
import com.kiwit.backend.dto.SignInResDTO;
import com.kiwit.backend.dto.SignUpReqDTO;
import com.kiwit.backend.repository.TrophyAwardedRepository;
import com.kiwit.backend.repository.TrophyRepository;
import com.kiwit.backend.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
public class ServiceTestHelper {

    UserService userService;
    TrophyRepository trophyRepository;
    TrophyAwardedRepository trophyAwardedRepository;
    JwtTokenProvider jwtTokenProvider;
    EntityManager em;

    ServiceTestHelper(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    ServiceTestHelper(UserService userService, JwtTokenProvider jwtTokenProvider,
                      TrophyRepository trophyRepository, TrophyAwardedRepository trophyAwardedRepository) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.trophyRepository = trophyRepository;
        this.trophyAwardedRepository = trophyAwardedRepository;
    }

    ServiceTestHelper(EntityManager em, UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.em = em;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;

    }

    public Long getNewUserId() {
        // Given
        String email = "spring@gmail.com";
        String nickname = "signUpTest";
        SignUpReqDTO signUpReqDTO = new SignUpReqDTO(email, nickname, Provider.KAKAO);

        // When
        SignInResDTO signInResDTO = userService.signUp(signUpReqDTO); // sign-up

        return jwtTokenProvider.getUserId(signInResDTO.getAccessToken());
    }

    public Long getNewUserId(String email, String nickname, Provider provider) {
        SignUpReqDTO signUpReqDTO = new SignUpReqDTO(email, nickname, provider);

        // When
        SignInResDTO signInResDTO = userService.signUp(signUpReqDTO); // sign-up

        return jwtTokenProvider.getUserId(signInResDTO.getAccessToken());
    }

    public SignInResDTO getNewUserToken() {
        // Given
        String email = "spring@gmail.com";
        String nickname = "signUpTest";
        SignUpReqDTO signUpReqDTO = new SignUpReqDTO(email, nickname, Provider.KAKAO);

        // When
        return userService.signUp(signUpReqDTO);
    }

    public List<TrophyAwarded> createNewTrophyAwarded(Long userId) {

        // Given
        String trophyName1 = "첫 번째 트로피";
        String trophyName2 = "두 번째 트로피";
        Trophy trophy1 = trophyRepository.save(new Trophy(null, trophyName1, "https://asd111.com"));
        Trophy trophy2 = trophyRepository.save(new Trophy(null, trophyName2, "https://asd222.com"));

        List<TrophyAwarded> trophyAwardedList = new ArrayList<>();

        trophyAwardedList.add(new TrophyAwarded(
                new TrophyAwardedId(userId, trophy1.getId()),
                new User(userId),
                trophy1
        ));
        trophyAwardedList.add(new TrophyAwarded(
                new TrophyAwardedId(userId, trophy2.getId()),
                new User(userId),
                trophy2
        ));

        trophyAwardedRepository.saveAll(trophyAwardedList);

        return trophyAwardedList;
    }

    /**
     * level 생성 (lv.1 ~ lv.5)
     */
    public List<Level> createLevels() {

        List<Level> levelList = new ArrayList<>();

        for (int i = 1; i < 6; i++) {
            Level level = new Level((long) i, i + "단계");
            em.persist(level);
            levelList.add(level);
        }

        return levelList;
    }

    /**
     * Category 생성 (테스트용 2개)
     */
    public List<Category> createCategories() {

        String[] titles = new String[] {
                "IT 교양", "자료구조",
//                "알고리즘", "데이터베이스", "네트워크",
//                "컴퓨터 아키텍쳐", "운영체제"
        };

        List<Category> categoryList = new ArrayList<>();

        for (int i = 1; i < titles.length + 1; i++) {
            Category category = new Category(
                    null, titles[i - 1], "https://" + titles[i - 1] + ".com");

            em.persist(category);
            categoryList.add(category);
        }

        return categoryList;
    }

    /**
     * 카테고리 내 챕터 생성
     * @param categoryList
     * : 본 함수 호출 이전에 createCategories 호출 후,
     * 생성된 카테고리를 인수로 넘겨주어야 함
     */
    public List<CategoryChapter> createCategoryChapters(List<Category> categoryList) {

        List<CategoryChapter> categoryChapterList = new ArrayList<>();

        for (Category category : categoryList) {
            CategoryChapter categoryChapter1 = new CategoryChapter(
                    null, "Ch. " + category.getId() + "-1", null, category, null
            );

            CategoryChapter categoryChapter2 = new CategoryChapter(
                    null, "Ch. " + category.getId() + "-2", null, category, null
            );

            categoryChapterList.add(categoryChapter1);
            categoryChapterList.add(categoryChapter2);

            em.persist(categoryChapter1);
            em.persist(categoryChapter2);
        }

        return categoryChapterList;
    }

    /**
     * 인수로 받은 챕터에 대하여
     * Content(각 2개)와 Payload(각 3개)를 생성
     * @param categoryChapterList
     * : Content를 생성할 대상 CategoryChapter
     * @param levelList
     * : 생성된 Level 리스트
     */
    public List<Content> createContentWithPayload(List<CategoryChapter> categoryChapterList, List<Level> levelList) {

        List<Content> contentList = new ArrayList<>();

        for (CategoryChapter chapter : categoryChapterList) {

            for (int i = 1; i < 3; i++) {

                Content content = new Content(
                        chapter.getId() + " 콘텐츠 (" + i + ")", i, 10, chapter.getId() + "예제 (" + i + ")",
                        true, levelList.get(i - 1), chapter
                );
                em.persist(content);

                for (int j = 1; j < 4; j++) {
                    ContentPayload contentPayload = new ContentPayload(
                            new ContentPayloadId(j, content.getId()),
                                    j % 2 == 0 ? ContentType.IMAGE : ContentType.TEXT,
                            "콘텐츠 내용 - " + j, content
                    );
                    em.persist(contentPayload);
                    content.getPayloadList().add(contentPayload);
                }

                contentList.add(content);
            }
        }

        return contentList;
    }

    /**
     * 문제 목록(QuizGroup) list 생성
     * Level과 CategoryChapter의 FK를 가지므로 이들을 인수로 받아서 사용
     * @param levelList
     * : 본 함수 호출 전 생성된 레벨 list
     * @param categoryChapterList
     * : 본 함수 호출 전 생성된 과목 챕터 list
     */
    public List<QuizGroup> createQuizGroup(List<Level> levelList, List<CategoryChapter> categoryChapterList) {

        List<QuizGroup> quizGroupList = new ArrayList<>();

        int i = 0;
        for (CategoryChapter categoryChapter : categoryChapterList) {

            QuizGroup quizGroup = new QuizGroup("문제 목록 " + i, "문제 목록 " + i +" 부제",
                    0, levelList.get(i % 5), categoryChapter);
            em.persist(quizGroup);
            quizGroupList.add(quizGroup);
            i++;
        }

        return quizGroupList;
    }

    /**
     * 인수로 받은 문제 목록(quizGroupList)에 대하여
     * 유형별 1개 quiz(+ choice) 생성
     * @param quizGroupList
     * : 문제를 생성할 타겟 목록 (본 함수 호출 이전 생성)
     */
    public  List<Quiz> createQuiz(List<QuizGroup> quizGroupList) {

        List<Quiz> quizList = new ArrayList<>();

        int i = 0;
        for (QuizGroup quizGroup: quizGroupList) {

            // 객관식
            Quiz quizMultiple = new Quiz("[" + i + "]" + "문제 1", QuizType.MULTIPLE,
                    "객관식 질문 1", "2", "객관식 해설 1", 20, quizGroup);
            em.persist(quizMultiple);

            // 객관식 선택지 1~4번
            for (int j = 1; j < 5; j++) {
                QuizChoice choice1 = new QuizChoice(
                        new QuizChoiceId(j, quizMultiple.getId()), "선택지 " + j + "번", quizMultiple);
                em.persist(choice1);
            }

            // TF(O/X) 퀴즈
            Quiz quizTF = new Quiz("[" + i + "]" + "문제 2", QuizType.TF,
                    "O/X 질문 1", "true", "O/X 해설 1", 10, quizGroup);
            em.persist(quizTF);

            // 단답형
            Quiz quizShort = new Quiz("[" + i + "]" + "문제 3", QuizType.SHORT,
                    "단답형 질문 1", "DFS", "단답형 해설 1", 30, quizGroup);
            em.persist(quizShort);

            quizGroup.setTotalScore(quizMultiple.getScore() + quizTF.getScore() + quizShort.getScore());
            quizGroup.getQuizList().add(quizMultiple);
            quizGroup.getQuizList().add(quizTF);
            quizGroup.getQuizList().add(quizShort);

            quizList.add(quizMultiple);
            quizList.add(quizTF);
            quizList.add(quizShort);
        }

        return quizList;
    }

}
