package com.kiwit.backend.service.impl;

import com.kiwit.backend.common.constant.QuizType;
import com.kiwit.backend.common.exception.CustomException;
import com.kiwit.backend.dao.*;
import com.kiwit.backend.domain.*;
import com.kiwit.backend.domain.compositeKey.QuizKeptId;
import com.kiwit.backend.dto.*;
import com.kiwit.backend.service.QuizService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements QuizService {

    private final QuizDAO quizDAO;
    private final QuizGroupDAO quizGroupDAO;
    private final QuizGroupSolvedDAO quizGroupSolvedDAO;
    private final QuizSolvedDAO quizSolvedDAO;
    private  final QuizKeptDAO quizKeptDAO;
    private final UserDAO userDAO;
    private final EntityManager em;

    @Autowired
    public QuizServiceImpl(QuizDAO quizDAO,
                           QuizGroupDAO quizGroupDAO,
                           QuizGroupSolvedDAO quizGroupSolvedDAO,
                           QuizSolvedDAO quizSolvedDAO,
                           QuizKeptDAO quizKeptDAO,
                           UserDAO userDAO,
                           EntityManager em) {
        this.quizDAO = quizDAO;
        this.quizGroupDAO = quizGroupDAO;
        this.quizGroupSolvedDAO = quizGroupSolvedDAO;
        this.quizSolvedDAO = quizSolvedDAO;
        this.quizKeptDAO = quizKeptDAO;
        this.userDAO = userDAO;
        this.em = em;
    }

    @Override
    public List<QuizGroupDTO> getQuizGroup(Integer next, Integer limit) {
        List<QuizGroup> quizGroupList = quizGroupDAO.selectGroupList(next, limit);

        List<QuizGroupDTO> quizGroupListDTO = new ArrayList<>();
        for (QuizGroup g : quizGroupList) {

            CategoryChapterDTO chapterDTO
                    = CategoryChapterDTO
                    .builder()
                    .id(g.getCategoryChapter().getId())
                    .title(g.getCategoryChapter().getTitle())
                    .build();

            QuizGroupDTO quizGroupDTO
                    = QuizGroupDTO
                    .builder()
                    .id(g.getId())
                    .title(g.getTitle())
                    .subtitle(g.getSubtitle())
                    .levelNum(g.getLevelNum())
                    .totalScore(g.getTotalScore())
                    .categoryChapter(chapterDTO)
                    .build();

            quizGroupListDTO.add(quizGroupDTO);
        }

        return quizGroupListDTO;
    }

    @Transactional
    @Override
    public QuizGroupWithQuizDTO solveQuizGroup(User user, Long groupId) {
//        QuizGroup quizGroup = quizGroupDAO.selectGroupWithQuiz(groupId);
        QuizGroup quizGroup = quizGroupDAO.selectGroup(groupId);
        List<Quiz> quizList = quizDAO.findQuizWithChoiceByGroupId(groupId);
        HashMap<Long, QuizKept> quizKeptHashMap = quizKeptDAO.selectQuizKeptInQuizIdList(
                user.getId(),
                quizList.stream().map(Quiz::getId).collect(Collectors.toList())
        );

        List<QuizDTO> quizDTOList = new ArrayList<>();

        for (Quiz q : quizList) {

            List<QuizChoiceDTO> quizChoiceDTOList = new ArrayList<>();

            if (q.getType() == QuizType.MULTIPLE) {
                for (QuizChoice choice : q.getChoiceList()) {
                    QuizChoiceDTO quizChoiceDTO
                            = QuizChoiceDTO
                            .builder()
                            .quizId(choice.getId().getQuizId())
                            .number(choice.getId().getNumber())
                            .payload(choice.getPayload())
                            .build();

                    quizChoiceDTOList.add(quizChoiceDTO);
                }
            }

            boolean isKept = quizKeptHashMap.containsKey(q.getId());

            QuizDTO quizDTO
                    = QuizDTO
                    .builder()
                    .id(q.getId())
                    .title(q.getTitle())
                    .type(q.getType())
                    .score(q.getScore())
                    .question(q.getQuestion())
                    .answer(q.getAnswer())
                    .explanation(q.getExplanation())
                    .choiceList(quizChoiceDTOList)
                    .kept(isKept)
                    .build();

            quizDTOList.add(quizDTO);
        }

        QuizGroupWithQuizDTO quizGroupDTO
                = QuizGroupWithQuizDTO
                .builder()
                .id(quizGroup.getId())
                .title(quizGroup.getTitle())
                .subtitle(quizGroup.getSubtitle())
                .levelNum(quizGroup.getLevelNum())
                .totalScore(quizGroup.getTotalScore())
                .quizList(quizDTOList)
                .build();


        return quizGroupDTO;
    }

    @Transactional
    @Override
    public QuizGroupSolvedDTO submitAnswers(User user, Long groupId, QuizAnswerListDTO quizAnswerListDTO) {

        User userProxy = userDAO.getUserProxy(user.getId());
        QuizGroup quizGroup = quizGroupDAO.selectGroupWithQuiz(groupId);

        // 최근 푼 문제 있으면 불러오고, 없으면 null 반환 (for latest score)
        QuizGroupSolved quizGroupSolvedLately = quizGroupSolvedDAO.selectGroupSolvedWithGroup(user.getId(), groupId);

        List<QuizAnswerDTO> answerDTOList = quizAnswerListDTO.getAnswerList();
        List<Quiz> quizList = quizGroup.getQuizList();

        if (answerDTOList.size() != quizList.size()) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }

        int scoreGot = 0;
        List<QuizSolved> quizSolvedList = new ArrayList<>();

        // 1. iterate dto and create entity. check user answers
        answerDTOList.sort(Comparator.comparing(QuizAnswerDTO::getQuizId));
        quizList.sort(Comparator.comparing(Quiz::getId));

        for (int i = 0; i < answerDTOList.size(); i++) {

            boolean correct = false;

            Quiz quiz = quizList.get(i);
            QuizAnswerDTO answer = answerDTOList.get(i);

            // check answer
            if (!answer.getQuizId().equals(quiz.getId())) {
                throw new CustomException(HttpStatus.BAD_REQUEST);
            }

            correct = answer.getAnswer().equals(quiz.getAnswer());
            if (correct) {
                scoreGot += quiz.getScore();
            }

            QuizSolved solved = QuizSolved
                    .builder()
                    .user(userProxy)
                    .quiz(quiz)
                    .myAnswer(answer.getAnswer())
                    .correct(correct)
                    .build();

            quizSolvedList.add(solved);
        }

        // 2. save quiz answers
        List<QuizSolved> savedQuizSolved = quizSolvedDAO.saveQuizSolvedList(quizSolvedList);

        // 3. save group solved result with calculated scores
        int highestScoreGot = scoreGot;
        if (quizGroupSolvedLately != null) {
            highestScoreGot = Math.max(scoreGot, quizGroupSolvedLately.getHighestScore());
        }

        QuizGroupSolved quizGroupSolved
                = QuizGroupSolved
                .builder()
                .user(userProxy)
                .quizGroup(quizGroup)
                .highestScore(highestScoreGot)
                .latestScore(scoreGot)
                .build();

        QuizGroupSolved savedQuizGroupSolved = quizGroupSolvedDAO.saveGroupSolved(quizGroupSolved);

        // 4. Generate Response DTO
        QuizGroupSolvedDTO quizGroupSolvedDTO
                = QuizGroupSolvedDTO
                .builder()
                .userId(savedQuizGroupSolved.getUser().getId())
                .quizGroupId(savedQuizGroupSolved.getQuizGroup().getId())
                .highestScore(savedQuizGroupSolved.getHighestScore())
                .latestScore(savedQuizGroupSolved.getLatestScore())
                .build();

        return quizGroupSolvedDTO;
    }

    @Override
    public QuizGroupWithSolvedDTO getQuizGroupLatestSolved(User authUser) {
        QuizGroupSolved quizGroupSolved = quizGroupSolvedDAO.selectGroupLatestSolved(authUser.getId());

        QuizGroupSolvedDTO quizGroupSolvedDTO
                = QuizGroupSolvedDTO
                .builder()
                .userId(quizGroupSolved.getUser().getId())
                .quizGroupId(quizGroupSolved.getQuizGroup().getId())
                .latestScore(quizGroupSolved.getLatestScore())
                .highestScore(quizGroupSolved.getHighestScore())
                .build();

        return QuizGroupWithSolvedDTO
                .builder()
                .id(quizGroupSolved.getQuizGroup().getId())
                .title(quizGroupSolved.getQuizGroup().getTitle())
                .subtitle(quizGroupSolved.getQuizGroup().getSubtitle())
                .levelNum(quizGroupSolved.getQuizGroup().getLevelNum())
                .totalScore(quizGroupSolved.getQuizGroup().getTotalScore())
                .result(quizGroupSolvedDTO)
                .build();
    }
    @Override
    public List<QuizGroupWithSolvedDTO> getQuizGroupSolved(User authUser, Integer next, Integer limit) {

        List<QuizGroupSolved> quizGroupSolvedList = quizGroupSolvedDAO.selectGroupSolved(authUser.getId(), next, limit);

        List<QuizGroupWithSolvedDTO> quizGroupWithSolvedDTOList = new ArrayList<>();
        for (QuizGroupSolved groupSolved : quizGroupSolvedList) {

            QuizGroupSolvedDTO quizGroupSolvedDTO
                    = QuizGroupSolvedDTO
                    .builder()
                    .userId(groupSolved.getUser().getId())
                    .quizGroupId(groupSolved.getQuizGroup().getId())
                    .latestScore(groupSolved.getLatestScore())
                    .highestScore(groupSolved.getHighestScore())
                    .build();

            QuizGroupWithSolvedDTO quizGroupWithSolvedDTO
                    = QuizGroupWithSolvedDTO
                    .builder()
                    .id(groupSolved.getQuizGroup().getId())
                    .title(groupSolved.getQuizGroup().getTitle())
                    .subtitle(groupSolved.getQuizGroup().getSubtitle())
                    .totalScore(groupSolved.getQuizGroup().getTotalScore())
                    .levelNum(groupSolved.getQuizGroup().getLevelNum())
                    .result(quizGroupSolvedDTO)
                    .build();

            quizGroupWithSolvedDTOList.add(quizGroupWithSolvedDTO);
        }

        return quizGroupWithSolvedDTOList;
    }

    @Override
    public List<QuizWithSolvedDTO> getQuizKept(User authUser, Integer next, Integer limit) {
        List<QuizKept> quizKeptList = quizKeptDAO.selectQuizKeptList(authUser.getId(), next, limit);

        List<QuizWithSolvedDTO> quizSolvedListDTO = new ArrayList<>();
        for (QuizKept quizKept : quizKeptList) {

            QuizSolvedDTO quizSolvedDTO = null;
            if (quizKept.getQuizSolved() != null) {

                quizSolvedDTO = QuizSolvedDTO
                        .builder()
                        .userId(quizKept.getQuizSolved().getUser().getId())
                        .quizId(quizKept.getQuizSolved().getQuiz().getId())
                        .correct(quizKept.getQuizSolved().getCorrect())
                        .myAnswer(quizKept.getQuizSolved().getMyAnswer())
                        .build();
            }


            QuizWithSolvedDTO quizDTO
                    = QuizWithSolvedDTO
                    .builder()
                    .id(quizKept.getQuiz().getId())
                    .type(quizKept.getQuiz().getType())
                    .title(quizKept.getQuiz().getTitle())
                    .question(quizKept.getQuiz().getQuestion())
                    .answer(quizKept.getQuiz().getAnswer())
                    .explanation(quizKept.getQuiz().getExplanation())
                    .score(quizKept.getQuiz().getScore())
                    .result(quizSolvedDTO)
                    .build();

            quizSolvedListDTO.add(quizDTO);
        }

        return quizSolvedListDTO;
    }

    @Transactional
    @Override
    public QuizKeptDTO keepQuiz(User authUser, Long quizId) {

        QuizKept quizKept = quizKeptDAO.selectQuizKept(quizId, authUser.getId());
        User userProxy = userDAO.getUserProxy(authUser.getId());
        Quiz quizProxy = quizDAO.getQuizProxy(quizId);

        QuizKept quizKeptCreated = null;

        if (quizKept == null) {
            QuizSolved quizSolvedLatest = quizSolvedDAO.selectQuizSolvedLatest(authUser.getId(), quizId);

            QuizKept quizKeptNew = QuizKept.builder()
                            .id(new QuizKeptId(authUser.getId(), quizId))
                                    .user(userProxy)
                                            .quiz(quizProxy)
                                                    .quizSolved(quizSolvedLatest)
                                                            .build();

            quizKeptCreated = quizKeptDAO.insertQuizKept(quizKeptNew);
        }
        else {
            quizKeptDAO.deleteQuizKept(quizKept);
        }

        QuizSolvedDTO quizSolvedDTO
                = (quizKeptCreated == null || quizKeptCreated.getQuizSolved() == null)
                ? null
                : QuizSolvedDTO.builder()
                    .userId(quizKeptCreated.getQuizSolved().getUser().getId())
                    .quizId(quizKeptCreated.getQuizSolved().getQuiz().getId())
                    .correct(quizKeptCreated.getQuizSolved().getCorrect())
                    .myAnswer(quizKeptCreated.getQuizSolved().getMyAnswer())
                    .build();


        QuizKeptDTO quizKeptDTO = QuizKeptDTO
                .builder()
                .userId(authUser.getId())
                .quizId(quizId)
                .quizSolved(quizSolvedDTO)
                .build();

        return quizKeptDTO;
    }
}
