package com.kiwit.backend.service.impl;

import com.kiwit.backend.common.constant.QuizType;
import com.kiwit.backend.common.exception.CustomException;
import com.kiwit.backend.dao.*;
import com.kiwit.backend.domain.*;
import com.kiwit.backend.domain.compositeKey.QuizGroupSolvedId;
import com.kiwit.backend.domain.compositeKey.QuizSolvedId;
import com.kiwit.backend.dto.*;
import com.kiwit.backend.service.QuizService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuizServiceImpl implements QuizService {

    private final QuizDAO quizDAO;
    private final QuizGroupDAO quizGroupDAO;
    private final QuizGroupSolvedDAO quizGroupSolvedDAO;
    private final QuizSolvedDAO quizSolvedDAO;
    private final UserDAO userDAO;

    @Autowired
    public QuizServiceImpl(QuizDAO quizDAO,
                           QuizGroupDAO quizGroupDAO,
                           QuizGroupSolvedDAO quizGroupSolvedDAO,
                           QuizSolvedDAO quizSolvedDAO,
                           UserDAO userDAO) {
        this.quizDAO = quizDAO;
        this.quizGroupDAO = quizGroupDAO;
        this.quizGroupSolvedDAO = quizGroupSolvedDAO;
        this.quizSolvedDAO = quizSolvedDAO;
        this.userDAO = userDAO;
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
    public QuizGroupWithQuizDTO solveQuizGroup(Long groupId) {
//        QuizGroup quizGroup = quizGroupDAO.selectGroupWithQuiz(groupId);
        QuizGroup quizGroup = quizGroupDAO.selectGroup(groupId);
        List<Quiz> quizList = quizDAO.findQuizWithChoiceByGroupId(groupId);

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
                    .id(new QuizSolvedId(user.getId(), answer.getQuizId()))
                    .user(userProxy)
                    .quiz(quiz)
                    .myAnswer(answer.getAnswer())
                    .correct(correct)
                    .kept(false) // @DynamicInsert 적용되지 않았기에 반드시 작성해야
                    .build();

            quizSolvedList.add(solved);
        }

        // 2. save quiz answers
        List<QuizSolved> savedQuizSolved = quizSolvedDAO.saveQuizSolvedList(quizSolvedList);

        // 3. save group solved result with calculated scores
        QuizGroupSolved quizGroupSolved
                = QuizGroupSolved
                .builder()
                .id(new QuizGroupSolvedId(user.getId(), groupId))
                .user(userProxy)
                .quizGroup(quizGroup)
                .highestScore(scoreGot)
                .latestScore(scoreGot)
                .build();

        QuizGroupSolved savedQuizGroupSolved = quizGroupSolvedDAO.saveGroupSolved(quizGroupSolved);

        // 4. Generate Response DTO
        QuizGroupSolvedDTO quizGroupSolvedDTO
                = QuizGroupSolvedDTO
                .builder()
                .userId(savedQuizGroupSolved.getId().getUserId())
                .quizGroupId(savedQuizGroupSolved.getId().getQuizGroupId())
                .highestScore(savedQuizGroupSolved.getHighestScore())
                .latestScore(savedQuizGroupSolved.getLatestScore())
                .build();

        return quizGroupSolvedDTO;
    }

    @Transactional
    @Override
    public QuizGroupSolvedDTO resubmitAnswers(User user, Long groupId, QuizAnswerListDTO quizAnswerListDTO) {

        // 1. get quiz solved with quiz (corresponding group)
        QuizGroupSolved quizGroupSolved = quizGroupSolvedDAO.selectGroupSolvedWithGroup(user.getId(), groupId);
        List<QuizSolved> quizSolvedList = quizSolvedDAO.selectQuizSolvedWithQuizByGroup(user.getId(), groupId);

        List<QuizAnswerDTO> answerDTOList = quizAnswerListDTO.getAnswerList();

        if (answerDTOList.size() != quizSolvedList.size()) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }

        // quizSolvedList already sorted while DB fetch
        answerDTOList.sort(Comparator.comparing(QuizAnswerDTO::getQuizId));

        int scoreGot = 0;

        for (int i = 0; i < answerDTOList.size(); i++) {

            boolean correct = false;

            QuizSolved quizSolved = quizSolvedList.get(i);
            QuizAnswerDTO answer = answerDTOList.get(i);

            // check answer
            if (!answer.getQuizId().equals(quizSolved.getId().getQuizId())) {
                throw new CustomException(HttpStatus.BAD_REQUEST);
            }

            correct = answer.getAnswer().equals(quizSolved.getQuiz().getAnswer());
            if (correct) {
                scoreGot += quizSolved.getQuiz().getScore();
            }

            // dirty check (update)
            quizSolved.setMyAnswer(answer.getAnswer());
            quizSolved.setCorrect(correct);
        }

        // update quiz group solved scores
        quizGroupSolved.setLatestScore(scoreGot);
        if (quizGroupSolved.getHighestScore() < scoreGot) {
            quizGroupSolved.setHighestScore(scoreGot);
        }

        // Generate Response DTO
        QuizGroupSolvedDTO quizGroupSolvedDTO
                = QuizGroupSolvedDTO
                .builder()
                .userId(quizGroupSolved.getId().getUserId())
                .quizGroupId(quizGroupSolved.getId().getQuizGroupId())
                .highestScore(quizGroupSolved.getHighestScore())
                .latestScore(quizGroupSolved.getLatestScore())
                .build();

        return quizGroupSolvedDTO;
    }

    @Override
    public QuizGroupWithSolvedDTO getQuizGroupLatestSolved(User authUser) {
        QuizGroupSolved quizGroupSolved = quizGroupSolvedDAO.selectGroupLatestSolved(authUser.getId());

        QuizGroupSolvedDTO quizGroupSolvedDTO
                = QuizGroupSolvedDTO
                .builder()
                .userId(quizGroupSolved.getId().getUserId())
                .quizGroupId(quizGroupSolved.getId().getQuizGroupId())
                .latestScore(quizGroupSolved.getLatestScore())
                .highestScore(quizGroupSolved.getHighestScore())
                .build();

        QuizGroupWithSolvedDTO quizGroupWithSolvedDTO
                = QuizGroupWithSolvedDTO
                .builder()
                .id(quizGroupSolved.getId().getQuizGroupId())
                .title(quizGroupSolved.getQuizGroup().getTitle())
                .subtitle(quizGroupSolved.getQuizGroup().getSubtitle())
                .result(quizGroupSolvedDTO)
                .build();

        return quizGroupWithSolvedDTO;
    }
    @Override
    public List<QuizGroupWithSolvedDTO> getQuizGroupSolved(User authUser, Integer next, Integer limit) {

        List<QuizGroupSolved> quizGroupSolvedList = quizGroupSolvedDAO.selectGroupSolved(authUser.getId(), next, limit);

        List<QuizGroupWithSolvedDTO> quizGroupWithSolvedDTOList = new ArrayList<>();
        for (QuizGroupSolved g : quizGroupSolvedList) {

            QuizGroupSolvedDTO quizGroupSolvedDTO
                    = QuizGroupSolvedDTO
                    .builder()
                    .userId(g.getId().getUserId())
                    .quizGroupId(g.getId().getQuizGroupId())
                    .latestScore(g.getLatestScore())
                    .highestScore(g.getHighestScore())
                    .build();

            QuizGroupWithSolvedDTO quizGroupWithSolvedDTO
                    = QuizGroupWithSolvedDTO
                    .builder()
                    .id(g.getId().getQuizGroupId())
                    .title(g.getQuizGroup().getTitle())
                    .subtitle(g.getQuizGroup().getSubtitle())
                    .totalScore(g.getQuizGroup().getTotalScore())
                    .result(quizGroupSolvedDTO)
                    .build();

            quizGroupWithSolvedDTOList.add(quizGroupWithSolvedDTO);
        }

        return quizGroupWithSolvedDTOList;
    }

    @Override
    public List<QuizWithSolvedDTO> getQuizKept(User authUser, Integer next, Integer limit) {
        List<QuizSolved> quizSolvedList = quizSolvedDAO.selectQuizKept(authUser.getId(), next, limit);

        List<QuizWithSolvedDTO> quizSolvedListDTO = new ArrayList<>();
        for (QuizSolved q : quizSolvedList) {

            QuizSolvedDTO quizSolvedDTO
                    = QuizSolvedDTO
                    .builder()
                    .userId(q.getId().getUserId())
                    .quizId(q.getId().getQuizId())
                    .correct(q.getCorrect())
                    .myAnswer(q.getMyAnswer())
                    .kept(q.getKept())
                    .build();

            QuizWithSolvedDTO quizDTO
                    = QuizWithSolvedDTO
                    .builder()
                    .id(q.getQuiz().getId())
                    .type(q.getQuiz().getType())
                    .title(q.getQuiz().getTitle())
                    .question(q.getQuiz().getQuestion())
                    .answer(q.getQuiz().getAnswer())
                    .explanation(q.getQuiz().getExplanation())
                    .score(q.getQuiz().getScore())
                    .result(quizSolvedDTO)
                    .build();

            quizSolvedListDTO.add(quizDTO);
        }

        return quizSolvedListDTO;
    }

    @Override
    public QuizSolvedDTO keepQuiz(User authUser, Long quizId) {
        QuizSolved quizSolved = quizSolvedDAO.keepQuiz(authUser.getId(), quizId);

        QuizSolvedDTO quizSolvedDTO = QuizSolvedDTO
                .builder()
                .userId(quizSolved.getId().getUserId())
                .quizId(quizSolved.getId().getQuizId())
                .correct(quizSolved.getCorrect())
                .myAnswer(quizSolved.getMyAnswer())
                .kept(quizSolved.getKept())
                .build();

        return quizSolvedDTO;
    }
}
