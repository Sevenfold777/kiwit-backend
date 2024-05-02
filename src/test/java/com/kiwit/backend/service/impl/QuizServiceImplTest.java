package com.kiwit.backend.service.impl;

import com.kiwit.backend.common.constant.QuizType;
import com.kiwit.backend.config.security.JwtTokenProvider;
import com.kiwit.backend.domain.*;
import com.kiwit.backend.dto.*;
import com.kiwit.backend.service.QuizService;
import com.kiwit.backend.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@DisplayName("QuizService Test")
class QuizServiceImplTest {

    private final QuizService quizService;
    private final EntityManager em;
    private final ServiceTestHelper serviceTestHelper;

    /* fields for global entities
       - list var name ends with "s" not "List" */
    private Long userId;
    private List<Level> levels;
    private List<Category> categories;
    private List<CategoryChapter> categoryChapters;
    private List<QuizGroup> quizGroups;
    private List<Quiz> quizzes;

    @Autowired
    QuizServiceImplTest(QuizService quizService,
                        UserService userService,
                        JwtTokenProvider jwtTokenProvider,
                        EntityManager em) {
        this.quizService = quizService;
        this.em = em;
        this.serviceTestHelper = new ServiceTestHelper(em, userService, jwtTokenProvider);
    }


    @Test
    @DisplayName("문제지 목록 조회 성공")
    public void getQuizGroupAllTest() {

        // Given
        initQuiz();

        // When
        List<QuizGroupDTO> quizGroupList = this.quizService.getQuizGroup(0, 20);

        // Then
        assertThat(quizGroupList.stream().map(QuizGroupDTO::getId).collect(toList()))
                .isEqualTo(quizGroups.stream().map(QuizGroup::getId).collect(toList()));
    }

    @Test
    @DisplayName("문제지 내용 조회 성공")
    public void getQuizGroupTest() {

        // Given
        initQuiz();
        QuizGroup quizGroup = quizGroups.get(0);
        List<Quiz> quizList = quizGroup.getQuizList();

        // When
        QuizGroupWithQuizDTO quizGroupWithQuizDTO = this.quizService.solveQuizGroup(quizGroup.getId());
        List<QuizDTO> quizDTOList = quizGroupWithQuizDTO.getQuizList();

        // Then
        assertThat(quizGroupWithQuizDTO.getId()).isEqualTo(quizGroup.getId());

        assertThat(quizDTOList.stream().map(q -> q.getId()).collect(toList()))
                .isEqualTo(quizList.stream().map(q -> q.getId()).collect(toList()));

        assertThat(quizDTOList.stream().map(q -> q.getType()).collect(toList()))
                .isEqualTo(quizList.stream().map(q -> q.getType()).collect(toList()));

        assertThat(quizGroupWithQuizDTO.getTotalScore())
                .isEqualTo(quizDTOList.stream().mapToInt(q -> q.getScore()).sum());

        quizDTOList.forEach(q -> {
            if (q.getType() == QuizType.MULTIPLE)
                assertThat(q.getChoiceList()).isNotEmpty();
            else
                assertThat(q.getChoiceList()).isEmpty();
        });


    }

    @Test
    @DisplayName("문제지 답안 제출(초시)")
    public void submitAnswersTest() {

        // Given
        initUser();
        initQuiz();
        QuizGroup quizGroup = quizGroups.get(0);
        QuizAnswerListDTO quizAnswerListDTO = createAnswerReq(quizGroup.getQuizList());

        // When
        QuizGroupSolvedDTO quizGroupSolvedDTO = this.quizService.submitAnswers(
                new User(userId), quizGroup.getId(), quizAnswerListDTO);

        // Then
        assertThat(quizGroupSolvedDTO.getQuizGroupId()).isEqualTo(quizGroup.getId());
        assertThat(quizGroupSolvedDTO.getLatestScore()).isEqualTo(30);
    }

    @Test
    @DisplayName("이미 제출한 문제지에 대해 초시 요청 시 에러")
    public void submitAnswersAgainFailTest() {

        // Given
        initUser();
        initQuiz();
        QuizGroup quizGroup = quizGroups.get(0);
        QuizAnswerListDTO quizAnswerListDTO = createAnswerReq(quizGroup.getQuizList());
        QuizGroupSolvedDTO quizGroupSolvedDTO = this.quizService.submitAnswers(
                new User(userId), quizGroup.getId(), quizAnswerListDTO);
        em.flush();
        em.clear();

        // When
        assertThrows(ConstraintViolationException.class,
                () -> {
                    this.quizService.submitAnswers(new User(userId), quizGroup.getId(), quizAnswerListDTO);
                    em.flush();
                });
    }

    @Test
    @DisplayName("문제지 답안 제출(재시)")
    public void resubmitAnswersTest() {
        fail("not implemented.");

    }

    @Test
    @DisplayName("최근 푼 문제지 조회 성공")
    public void getQuizGroupLatestSolvedSuccessTest() {
        fail("not implemented.");

    }

    @Test
    @DisplayName("최근 푼 문제지 조회 - 푼 문제 없을 때")
    public void getQuizGroupLatestSolvedNoneTest() {
        fail("not implemented.");

    }

    @Test
    @DisplayName("푼 문제 목록 조회")
    public void getQuizGroupSolvedTest() {
        fail("not implemented.");

    }

    @Test
    @DisplayName("문제 보관 테스트")
    public void keepQuizTest() {
        fail("not implemented.");

    }


    private void initUser() {
        userId = serviceTestHelper.getNewUserId();

        em.flush();
        em.clear();
    }

    private void initQuiz() {
        levels = serviceTestHelper.createLevels(); // init Level
        categories = serviceTestHelper.createCategories(); // init Category
        categoryChapters = serviceTestHelper.createCategoryChapters(categories); // init Chapter
        quizGroups = serviceTestHelper.createQuizGroup(levels, categoryChapters); // init QuizGroup
        quizzes = serviceTestHelper.createQuiz(quizGroups); // init Quiz (+ Choice if necessary)

        em.flush();
        em.clear();
    }

    private QuizAnswerListDTO createAnswerReq(List<Quiz> quizList) {

        QuizAnswerListDTO result = new QuizAnswerListDTO(new ArrayList<>());

        for (Quiz quiz : quizList) {

            QuizAnswerDTO quizAnswerDTO;

            switch (quiz.getType()) {
                // initQuiz()에서 유형별 1개씩 Quiz entity 저장
                case MULTIPLE -> quizAnswerDTO = new QuizAnswerDTO(quiz.getId(), "2"); // (O) 20/20점
                case TF -> quizAnswerDTO = new QuizAnswerDTO(quiz.getId(), "true"); // (O) 10/10점
                case SHORT -> quizAnswerDTO = new QuizAnswerDTO(quiz.getId(), "BFS"); // (X) 0/30점
                default -> throw new IllegalStateException("Unexpected value: " + quiz.getType());
            }

            result.getAnswerList().add(quizAnswerDTO);
        }

        return result;
    }

}