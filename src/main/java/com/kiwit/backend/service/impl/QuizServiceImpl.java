package com.kiwit.backend.service.impl;

import com.kiwit.backend.common.constant.QuizType;
import com.kiwit.backend.dao.QuizDAO;
import com.kiwit.backend.dao.QuizGroupDAO;
import com.kiwit.backend.dao.QuizGroupSolvedDAO;
import com.kiwit.backend.dao.QuizSolvedDAO;
import com.kiwit.backend.domain.*;
import com.kiwit.backend.domain.compositeKey.QuizGroupSolvedId;
import com.kiwit.backend.domain.compositeKey.QuizSolvedId;
import com.kiwit.backend.dto.*;
import com.kiwit.backend.service.QuizService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

            // should be changed to enum
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
        // TODO
        // if call this function for already solved problem,
        // even not answer changed
        // update query fires...

        QuizGroup quizGroup = quizGroupDAO.selectGroupWithQuiz(groupId);

        Integer scoreGot = 0;
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
                    scoreGot += quiz.get().getScore();
                }
            } else {
                // throw exception
                 return null;
            }

            QuizSolved solved = QuizSolved
                    .builder()
                    .id(new QuizSolvedId(user.getId(), answer.getQuizId()))
                    .user(new User(user.getId()))
                    .quiz(new Quiz(answer.getQuizId()))
                    .myAnswer(answer.getAnswer())
                    .correct(correct)
                    .kept(false) // column default 확인하기
                    .build();

            quizSolvedList.add(solved);
        }

        // 2. save quiz answers
        List<QuizSolved> savedQuizSolved = quizSolvedDAO.saveQuizSolvedList(quizSolvedList);

        QuizGroupSolved quizGroupSolved
                = QuizGroupSolved
                .builder()
                .id(new QuizGroupSolvedId(user.getId(), groupId))
                .user(new User(user.getId()))
                .quizGroup(new QuizGroup(groupId))
                .highestScore(scoreGot)
                .latestScore(scoreGot)
                .build();

        // 3. save group solved result with calculated scores
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
        List<QuizSolved> quizSolvedList = quizSolvedDAO.selectQuizSolvedWithQuizByGroup(user.getId(), groupId);
        QuizGroupSolved quizGroupSolved = quizGroupSolvedDAO.selectGroupSolvedWithGroup(user.getId(), groupId);

        Integer scoreGot = 0;

        for (QuizSolved s : quizSolvedList) {

            Boolean correct = false;

            // 2. check answer
            Optional<QuizAnswerDTO> answerDTO = quizAnswerListDTO.getAnswerList().stream()
                                .filter(a -> a.getQuizId().equals(s.getId().getQuizId())).findAny();

            String answerNew = answerDTO.get().getAnswer();

            // check correct and add score
            if (answerDTO.isPresent()) {
                correct = answerNew.equals(s.getQuiz().getAnswer());
                if (correct) {
                    scoreGot += s.getQuiz().getScore();
                }
            } else {
                // throw exception
                return null;
            }

            // 3. update quiz solved
            s.setMyAnswer(answerNew);
            s.setCorrect(correct);

        }

        // 4. update quiz group solved scores
        quizGroupSolved.setLatestScore(scoreGot);
        if (quizGroupSolved.getHighestScore() < scoreGot) {
            quizGroupSolved.setHighestScore(scoreGot);
        }

        // 4. Generate Response DTO
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
