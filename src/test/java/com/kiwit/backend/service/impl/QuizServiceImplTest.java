package com.kiwit.backend.service.impl;

import com.kiwit.backend.common.constant.QuizType;
import com.kiwit.backend.common.exception.CustomException;
import com.kiwit.backend.config.security.JwtTokenProvider;
import com.kiwit.backend.domain.*;
import com.kiwit.backend.domain.compositeKey.QuizGroupSolvedId;
import com.kiwit.backend.domain.compositeKey.QuizSolvedId;
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
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        assertThat(quizGroupSolvedDTO.getLatestScore()).isEqualTo(quizGroupSolvedDTO.getHighestScore());
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
    @DisplayName("초시: groupId와 quiz.groupId 매치되지 않을 경우 에러")
    public void submitAnswerNotCorrespondingGroupIdFailTest() {

        // Given
        initUser();
        initQuiz();
        QuizGroup quizGroup1 = quizGroups.get(0);
        QuizGroup quizGroup2 = quizGroups.get(1);
        QuizAnswerListDTO quizAnswerListDTO = createAnswerReq(quizGroup1.getQuizList());

        // When (quizGroup2.getId() 주의!)
        assertThrows(CustomException.class,
                () -> this.quizService.submitAnswers(new User(userId), quizGroup2.getId(), quizAnswerListDTO));
    }

    @Test
    @DisplayName("초시: ReqDTO와 quizGroup.quizList의 size가 다를 경우 에러")
    public void submitAnswerWithInvalidReqBodyFailTest() {

        // Given
        initUser();
        initQuiz();
        QuizGroup quizGroup1 = quizGroups.get(0);
        QuizAnswerListDTO quizAnswerListDTO = createAnswerReq(quizGroup1.getQuizList());
        quizAnswerListDTO.getAnswerList().add(new QuizAnswerDTO(1L, "answerFail"));

        // When
        assertThrows(CustomException.class,
                () -> this.quizService.submitAnswers(new User(userId), quizGroup1.getId(), quizAnswerListDTO));
    }

    @Test
    @DisplayName("초시: randomly-sorted answerList")
    public void submitAnswerWithUnsortedReqBodyTest() {
        // !!! When 바로 위 answerList reverse 제외 resubmitAnswersTest 와 동일

        // Given
        initUser();
        initQuiz();
        QuizGroup quizGroup = quizGroups.get(0);
        QuizAnswerListDTO quizAnswerListDTO = createAnswerReq(quizGroup.getQuizList());

        Collections.reverse(quizAnswerListDTO.getAnswerList()); // reverse answerList Req(input)

        // When
        QuizGroupSolvedDTO quizGroupSolvedDTO = this.quizService.submitAnswers(
                new User(userId), quizGroup.getId(), quizAnswerListDTO);

        // Then
        assertThat(quizGroupSolvedDTO.getQuizGroupId()).isEqualTo(quizGroup.getId());
        assertThat(quizGroupSolvedDTO.getLatestScore()).isEqualTo(30);
        assertThat(quizGroupSolvedDTO.getLatestScore()).isEqualTo(quizGroupSolvedDTO.getHighestScore());
    }

    @Test
    @DisplayName("문제지 답안 제출(재시)")
    public void resubmitAnswersTest() {

        // Given
        initUser();
        initQuiz();
        QuizGroup quizGroup = quizGroups.get(0);
        QuizAnswerListDTO quizAnswerListDTO1 = createAnswerReq(quizGroup.getQuizList());

        QuizGroupSolvedDTO quizGroupSolvedDTO = this.quizService.submitAnswers(
                new User(userId), quizGroup.getId(), quizAnswerListDTO1);
        em.flush();
        em.clear();

        QuizAnswerListDTO quizAnswerListDTO2
                = createAnswerReq(quizGroup.getQuizList(), "1", "true", "DFS");

        List<Long> quizIdList = quizAnswerListDTO2.getAnswerList()
                .stream().map(QuizAnswerDTO::getQuizId).collect(toList());

        // When
        QuizGroupSolvedDTO groupResubmittedDTO
                = this.quizService.resubmitAnswers(new User(userId), quizGroup.getId(), quizAnswerListDTO2);

        // Then

        // 1. Check if answer successfully modified
        String queryStr = "select qs from QuizSolved qs" +
                " where qs.quiz.id in :quizIdList" +
                " order by qs.id.quizId asc";
        List<QuizSolved> modifiedQuizSolvedList = em.createQuery(queryStr, QuizSolved.class)
                .setParameter("quizIdList", quizIdList)
                .getResultList();
        // => JPQL 이므로 무조건 select query가 DB에 전송

        assertThat(modifiedQuizSolvedList.stream().map(qs -> qs.getMyAnswer()).collect(toList()))
                .isEqualTo(Arrays.asList("1", "true", "DFS"));


        // 2. Check if score correctly calculated
        QuizGroupSolved modifiedQuizGroupSolved
                = em.find(QuizGroupSolved.class, new QuizGroupSolvedId(userId, quizGroup.getId()));
        // => 1차 캐시에서 찾아 옴, select query 발생 X

        assertThat(modifiedQuizGroupSolved.getLatestScore())
                .isEqualTo(40);
    }

    @Test
    @DisplayName("재시: 아직 안 푼 문제 수정 시 에러")
    public void resubmitAnswerToNotSubmittedFailTest() {

        // Given
        initUser();
        initQuiz();
        QuizGroup quizGroup = quizGroups.get(0);
        QuizAnswerListDTO answerListDTO = createAnswerReq(quizGroup.getQuizList());

        // when
        assertThrows(DataAccessException.class,
                () -> this.quizService.resubmitAnswers(new User(userId), quizGroup.getId(), answerListDTO));
    }

    @Test
    @DisplayName("재시: groupId와 quiz.groupId 매치되지 않을 경우 에러")
    public void resubmitAnswerNotCorrespondingGroupIdFailTest() {

        // Given
        initUser();
        initQuiz();

        QuizGroup quizGroup1 = quizGroups.get(0);
        QuizGroup quizGroup2 = quizGroups.get(1);
        QuizAnswerListDTO answerListDTO1 = createAnswerReq(quizGroup1.getQuizList());
        QuizAnswerListDTO answerListDTO2 = createAnswerReq(quizGroup2.getQuizList());

        QuizGroupSolvedDTO quizGroupSolvedDTO1
                = this.quizService.submitAnswers(new User(userId), quizGroup1.getId(), answerListDTO1);

        // When
        CustomException e = assertThrows(CustomException.class,
                () -> this.quizService.resubmitAnswers(new User(userId), quizGroup1.getId(), answerListDTO2));

        assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("재시: ReqDTO와 quizGroup.quizList의 size가 다를 경우 에러")
    public void resubmitAnswerWithInvalidReqBodyFailTest() {

        // Given
        initUser();
        initQuiz();

        QuizGroup quizGroup1 = quizGroups.get(0);
        QuizAnswerListDTO answerListDTO = createAnswerReq(quizGroup1.getQuizList());

        QuizGroupSolvedDTO quizGroupSolvedDTO =
                this.quizService.submitAnswers(new User(userId), quizGroup1.getId(), answerListDTO);

        answerListDTO.getAnswerList().add(new QuizAnswerDTO(1L, "fail answer"));

        // When
        CustomException e = assertThrows(CustomException.class,
                () -> this.quizService.resubmitAnswers(new User(userId), quizGroup1.getId(), answerListDTO));

        assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("재시: randomly-sorted answerList")
    public void resubmitAnswerWithUnsortedReqBodyTest() {
        // !!! When 바로 위 answerList reverse 제외 resubmitAnswersTest 와 동일

        // Given
        initUser();
        initQuiz();
        QuizGroup quizGroup = quizGroups.get(0);
        QuizAnswerListDTO quizAnswerListDTO1 = createAnswerReq(quizGroup.getQuizList());

        QuizGroupSolvedDTO quizGroupSolvedDTO = this.quizService.submitAnswers(
                new User(userId), quizGroup.getId(), quizAnswerListDTO1);
        em.flush();
        em.clear();

        QuizAnswerListDTO quizAnswerListDTO2
                = createAnswerReq(quizGroup.getQuizList(), "1", "true", "DFS");

        List<Long> quizIdList = quizAnswerListDTO2.getAnswerList()
                .stream().map(QuizAnswerDTO::getQuizId).collect(toList());

        Collections.reverse(quizAnswerListDTO2.getAnswerList()); // reverse answerList Req(input)

        // When
        QuizGroupSolvedDTO groupResubmittedDTO
                = this.quizService.resubmitAnswers(new User(userId), quizGroup.getId(), quizAnswerListDTO2);

        // Then

        // 1. Check if answer successfully modified
        String queryStr = "select qs from QuizSolved qs" +
                " where qs.quiz.id in :quizIdList" +
                " order by qs.id.quizId asc";
        List<QuizSolved> modifiedQuizSolvedList = em.createQuery(queryStr, QuizSolved.class)
                .setParameter("quizIdList", quizIdList)
                .getResultList();
        // => JPQL 이므로 무조건 select query가 DB에 전송

        assertThat(modifiedQuizSolvedList.stream().map(qs -> qs.getMyAnswer()).collect(toList()))
                .isEqualTo(Arrays.asList("1", "true", "DFS"));


        // 2. Check if score correctly calculated
        QuizGroupSolved modifiedQuizGroupSolved
                = em.find(QuizGroupSolved.class, new QuizGroupSolvedId(userId, quizGroup.getId()));
        // => 1차 캐시에서 찾아 옴, select query 발생 X

        assertThat(modifiedQuizGroupSolved.getLatestScore())
                .isEqualTo(40);
    }

    @Test
    @DisplayName("최근 푼 문제지 조회 성공")
    public void getQuizGroupLatestSolvedSuccessTest() {

        // Given
        initUser();
        initQuiz();
        QuizGroup quizGroup = quizGroups.get(0);
        QuizAnswerListDTO quizAnswerListDTO = createAnswerReq(quizGroup.getQuizList());

        List<Long> quizIdList = quizAnswerListDTO.getAnswerList()
                .stream().map(QuizAnswerDTO::getQuizId).collect(toList());

        // When
        QuizGroupSolvedDTO quizGroupSolvedDTO = this.quizService.submitAnswers(
                new User(userId), quizGroup.getId(), quizAnswerListDTO);

        // Then
        QuizGroupWithSolvedDTO quizGroupLatestSolved = this.quizService.getQuizGroupLatestSolved(new User(userId));
        assertThat(quizGroupLatestSolved.getId()).isEqualTo(quizGroupSolvedDTO.getQuizGroupId());
        assertThat(quizGroupLatestSolved.getResult().getLatestScore()).isEqualTo(quizGroupSolvedDTO.getLatestScore());

        String queryStr = "select qs from QuizSolved qs" +
                " where qs.quiz.id in :quizIdList" +
                " order by qs.id.quizId asc";
        List<QuizSolved> savedQuizSolvedIdList = em.createQuery(queryStr, QuizSolved.class)
                .setParameter("quizIdList", quizIdList)
                .getResultList();
        assertThat(savedQuizSolvedIdList.stream().map(qs -> qs.getId().getQuizId()).collect(toList()))
                .isEqualTo(quizGroup.getQuizList().stream().map(Quiz::getId).collect(toList()));
    }

    @Test
    @DisplayName("최근 푼 문제지 조회 - 푼 문제 없을 때")
    public void getQuizGroupLatestSolvedNoneTest() {

        // Given
        initUser();
        initQuiz();

        // When
        CustomException e = assertThrows(CustomException.class,
                () -> this.quizService.getQuizGroupLatestSolved(new User(userId)));
        assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("푼 문제 목록 조회")
    public void getQuizGroupSolvedTest() {

        // Given
        initUser();
        initQuiz();
        QuizGroup quizGroup1 = quizGroups.get(0);
        QuizAnswerListDTO quizAnswerListDTO1 = createAnswerReq(quizGroup1.getQuizList());

        QuizGroup quizGroup2 = quizGroups.get(1);
        QuizAnswerListDTO quizAnswerListDTO2 = createAnswerReq(quizGroup2.getQuizList());

        // When
        QuizGroupSolvedDTO quizGroupSolvedDTO1 = this.quizService.submitAnswers(
                new User(userId), quizGroup1.getId(), quizAnswerListDTO1);

        QuizGroupSolvedDTO quizGroupSolvedDTO2 = this.quizService.submitAnswers(
                new User(userId), quizGroup2.getId(), quizAnswerListDTO2);

        // Then
        List<QuizGroupWithSolvedDTO> quizGroupSolvedDTOList
                = this.quizService.getQuizGroupSolved(new User(userId), 0, 20);

        assertThat(quizGroupSolvedDTOList.stream().map(QuizGroupWithSolvedDTO::getId).collect(toList()))
                .isEqualTo(Arrays.asList(quizGroup2.getId(), quizGroup1.getId()));
        assertThat(quizGroupSolvedDTOList.get(0).getResult().getLatestScore()).isEqualTo(30);
        assertThat(quizGroupSolvedDTOList.get(1).getResult().getLatestScore()).isEqualTo(30);

    }

    @Test
    @DisplayName("문제 보관 테스트")
    public void keepQuizTest() {

        // Given
        initUser();
        initQuiz();
        QuizGroup quizGroup = quizGroups.get(0);
        QuizAnswerListDTO quizAnswerListDTO = createAnswerReq(quizGroup.getQuizList());

        QuizGroupSolvedDTO quizGroupSolvedDTO = this.quizService.submitAnswers(
                new User(userId), quizGroup.getId(), quizAnswerListDTO);
        Long tgtQuizId = quizAnswerListDTO.getAnswerList().get(0).getQuizId();

        // When
        QuizSolvedDTO quizKeptDTO = this.quizService.keepQuiz(
                new User(userId), tgtQuizId);

        // Then
        QuizSolved quizSolved = em.find(QuizSolved.class, new QuizSolvedId(userId, tgtQuizId));

        assertThat(quizKeptDTO.getQuizId()).isEqualTo(quizSolved.getId().getQuizId());
        assertThat(quizSolved.getKept()).isEqualTo(true);
    }

    @Test
    @DisplayName("문제 보관 테스트 실패 - 아직 안 푼 문제")
    public void keepQuizInvalidInputFailTest() {

        // Given
        initUser();
        initQuiz();
        Long tgtQuizId = quizzes.get(0).getId();

        // When
        DataAccessException e = assertThrows(DataAccessException.class,
                () -> this.quizService.keepQuiz(new User(userId), tgtQuizId));
        assertThat(e.getMessage()).isEqualTo("Cannot find Quiz Solved with userId and quizId");
    }

    @Test
    @DisplayName("보관한 문제 리스트 테스트")
    public void getQuizKeptTest() {

        // Given
        initUser();
        initQuiz();
        QuizGroup quizGroup = quizGroups.get(0);
        QuizAnswerListDTO quizAnswerListDTO = createAnswerReq(quizGroup.getQuizList());

        QuizGroupSolvedDTO quizGroupSolvedDTO = this.quizService.submitAnswers(
                new User(userId), quizGroup.getId(), quizAnswerListDTO);
        Long tgtQuizId = quizAnswerListDTO.getAnswerList().get(0).getQuizId();

        // When
        QuizSolvedDTO quizKeptDTO = this.quizService.keepQuiz(
                new User(userId), tgtQuizId);

        // Then
        List<QuizWithSolvedDTO> quizKeptDTOList = this.quizService.getQuizKept(new User(userId), 0, 20);

        assertThat(quizKeptDTOList.size()).isEqualTo(1);
        assertThat(quizKeptDTOList.get(0).getId()).isEqualTo(tgtQuizId);
    }

    @Test
    @DisplayName("보관한 문제 없을 때 테스트")
    public void getQuizKeptNoneTest() {

        // Given
        initUser();

        // When
        List<QuizWithSolvedDTO> quizKeptDTOList = this.quizService.getQuizKept(new User(userId), 0, 20);

        // Then
        assertThat(quizKeptDTOList.size()).isEqualTo(0);
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

    /**
     * Default (no answer input) QuizAnswerListDTO
     * Multiple: (O) 20/20점
     * TF: (O) 10/10점
     * Short: (X) 0/30점
     */
    private QuizAnswerListDTO createAnswerReq(List<Quiz> quizList) {

        String answerMul = "2";
        String answerTF = "true";
        String answerShort = "BFS";

        return createAnswerReq(quizList, answerMul, answerTF, answerShort);
    }

    private QuizAnswerListDTO createAnswerReq(List<Quiz> quizList, String answerMul, String answerTF, String answerShort) {

        QuizAnswerListDTO result = new QuizAnswerListDTO(new ArrayList<>());

        for (Quiz quiz : quizList) {

            QuizAnswerDTO quizAnswerDTO;

            switch (quiz.getType()) {
                // initQuiz()에서 유형별 1개씩 Quiz entity 저장
                case MULTIPLE -> quizAnswerDTO = new QuizAnswerDTO(quiz.getId(), answerMul);
                case TF -> quizAnswerDTO = new QuizAnswerDTO(quiz.getId(), answerTF);
                case SHORT -> quizAnswerDTO = new QuizAnswerDTO(quiz.getId(), answerShort);
                default -> throw new IllegalStateException("Unexpected value: " + quiz.getType());
            }

            result.getAnswerList().add(quizAnswerDTO);
        }

        return result;
    }

}