package com.kiwit.backend.service.impl;

import com.kiwit.backend.dao.QuizDAO;
import com.kiwit.backend.dao.QuizGroupDAO;
import com.kiwit.backend.dao.QuizGroupSolvedDAO;
import com.kiwit.backend.dao.QuizSolvedDAO;
import com.kiwit.backend.domain.*;
import com.kiwit.backend.domain.compositeKey.QuizGroupSolvedId;
import com.kiwit.backend.domain.compositeKey.QuizSolvedId;
import com.kiwit.backend.dto.*;
import com.kiwit.backend.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizServiceImpl implements QuizService {

    private final QuizDAO quizDAO;
    private final QuizGroupDAO quizGroupDAO;
    private final QuizGroupSolvedDAO quizGroupSolvedDAO;
    private final QuizSolvedDAO quizSolvedDAO;

    @Autowired
    public QuizServiceImpl(QuizDAO quizDAO,
                           QuizGroupDAO quizGroupDAO,
                           QuizGroupSolvedDAO quizGroupSolvedDAO,
                           QuizSolvedDAO quizSolvedDAO) {
        this.quizDAO = quizDAO;
        this.quizGroupDAO = quizGroupDAO;
        this.quizGroupSolvedDAO = quizGroupSolvedDAO;
        this.quizSolvedDAO = quizSolvedDAO;
    }

    @Override
    public List<QuizGroupDTO> getQuizGroup() {
        List<QuizGroup> quizGroupList = quizGroupDAO.selectGroupList();

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
                    .categoryChapter(chapterDTO)
                    .build();

            quizGroupListDTO.add(quizGroupDTO);
        }

        return quizGroupListDTO;
    }
    @Override
    public QuizGroupWithQuizDTO solveQuizGroup(Long groupId) {
        QuizGroup quizGroup = quizGroupDAO.selectGroupWithQuiz(groupId);

        List<QuizDTO> quizDTOList = new ArrayList<>();

        for (Quiz q : quizGroup.getQuizList()) {

            List<QuizChoiceDTO> quizChoiceDTOList = new ArrayList<>();

            // should be changed to enum
            if (q.getType().equals("multiple")) {
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
                .quizList(quizDTOList)
                .build();


        return quizGroupDTO;
    }
    @Override
    public QuizGroupSolvedDTO submitAnswers(Long userId, Long groupId, QuizAnswerListDTO quizAnswerListDTO) {

        QuizGroup quizGroup = quizGroupDAO.selectGroupWithQuiz(groupId);

        Integer totalScore = 0;
        List<QuizSolved> quizSolvedList = new ArrayList<>();

        // 1. iterate dto and create entity. check user answers
        for (QuizAnswerDTO answer : quizAnswerListDTO.getAnswerList()) {

            Boolean correct = false;

            // check answer
            Optional<Quiz> quiz =  quizGroup.getQuizList().stream()
                    .filter(q -> q.getId().equals(answer.getQuizId())).findAny();

            if (quiz.isPresent()) {
                correct = answer.getAnswer().equals(quiz.get().getAnswer());
                if (correct) {
                    totalScore += quiz.get().getScore();
                }
            } else {
                // throw exception
                 return null;
            }

            QuizSolved solved = QuizSolved
                    .builder()
                    .id(new QuizSolvedId(userId, groupId))
                    .myAnswer(answer.getAnswer())
                    .correct(correct)
                    .kept(false) // default 확인하기
                    .build();

            quizSolvedList.add(solved);
        }

        // 2. save quiz answers
        List<QuizSolved> savedQuizSolved = quizSolvedDAO.insertQuizSolvedList(quizSolvedList);

        QuizGroupSolved quizGroupSolved
                = QuizGroupSolved
                .builder()
                .id(new QuizGroupSolvedId(userId, groupId))
                .highestScore(totalScore)
                .latestScore(totalScore)
                .build();

        // 3. save group solved result with calculated scores
        QuizGroupSolved savedQuizGroupSolved = quizGroupSolvedDAO.insertGroupSolved(quizGroupSolved);

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
    @Override
    public QuizGroupSolvedDTO resubmitAnswers(Long userId, Long groupId, QuizAnswerListDTO quizAnswerListDTO) {

        // iterate dto and create entity. check user answers

        // save result of quiz group

        return null;
    }

    @Override
    public QuizGroupWithSolvedDTO getQuizGroupLatestSolved(User authUser) {
        QuizGroupSolved quizGroupSolved = quizGroupSolvedDAO.selectGroupLatestSolved(authUser.getId());

        QuizGroupWithSolvedDTO quizGroupWithSolvedDTO
                = QuizGroupWithSolvedDTO
                .builder()
                .id(quizGroupSolved.getQuizGroup().getId())
                .title(quizGroupSolved.getQuizGroup().getTitle())
                .subtitle(quizGroupSolved.getQuizGroup().getSubtitle())
//                .result(quizGroupSolved.getUser().getQuizGroupSolvedList().get(0))
                .build();

        return quizGroupWithSolvedDTO;
    }
    @Override
    public List<QuizWithSolvedDTO> getQuizKept(User authUser, Integer next, Integer limit) {
        List<QuizSolved> quizSolvedList = quizSolvedDAO.selectQuizKept(authUser.getId());

        List<QuizWithSolvedDTO> quizSolvedListDTO = new ArrayList<>();
        for (QuizSolved g : quizSolvedList) {
            QuizWithSolvedDTO quizDTO
                    = QuizWithSolvedDTO
                    .builder()
                    .id(g.getQuiz().getId())
                    .type(g.getQuiz().getType())
                    .title(g.getQuiz().getTitle())
                    .question(g.getQuiz().getQuestion())
                    .answer(g.getQuiz().getAnswer())
                    .explanation(g.getQuiz().getExplanation())
                    .score(g.getQuiz().getScore())
//                    .result
                    .build();

            quizSolvedListDTO.add(quizDTO);
        }

        return quizSolvedListDTO;
    }
    @Override
    public List<QuizWithSolvedDTO> getQuizSolved(User authUser, Integer next, Integer limit) {

        List<QuizSolved> quizSolvedList = quizSolvedDAO.selectQuizSolved(authUser.getId());

        List<QuizWithSolvedDTO> quizSolvedListDTO = new ArrayList<>();
        for (QuizSolved g : quizSolvedList) {
            QuizWithSolvedDTO quizDTO
                    = QuizWithSolvedDTO
                    .builder()
                    .id(g.getQuiz().getId())
                    .type(g.getQuiz().getType())
                    .title(g.getQuiz().getTitle())
                    .question(g.getQuiz().getQuestion())
                    .answer(g.getQuiz().getAnswer())
                    .explanation(g.getQuiz().getExplanation())
                    .score(g.getQuiz().getScore())
//                    .result
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
