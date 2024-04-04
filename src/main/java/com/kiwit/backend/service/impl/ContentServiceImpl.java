package com.kiwit.backend.service.impl;

import com.kiwit.backend.dao.*;
import com.kiwit.backend.domain.*;
import com.kiwit.backend.domain.compositeKey.ContentStudiedId;
import com.kiwit.backend.dto.*;
import com.kiwit.backend.service.ContentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    private final ContentDAO contentDAO;
    private final LevelDAO levelDAO;
    private final ContentStudiedDAO contentStudiedDAO;
    private final CategoryDAO categoryDAO;
    private final CategoryChapterDAO categoryChapterDAO;

    @Autowired
    public ContentServiceImpl(ContentDAO contentDAO,
                              LevelDAO levelDAO,
                              ContentStudiedDAO contentStudiedDAO,
                              CategoryDAO categoryDAO,
                              CategoryChapterDAO categoryChapterDAO) {
        this.contentDAO = contentDAO;
        this.levelDAO = levelDAO;
        this.contentStudiedDAO = contentStudiedDAO;
        this.categoryDAO = categoryDAO;
        this.categoryChapterDAO = categoryChapterDAO;
    }

    @Override
    public List<LevelDTO> getLevelList() {
        List<Level> levelList = levelDAO.selectLevelList();

        List<LevelDTO> levelDTOList = new ArrayList<>();

        for (Level level: levelList) {
            levelDTOList.add(new LevelDTO(level.getNum(), level.getTitle()));
        }

        return levelDTOList;
    }

    @Override
    public List<ContentDTO> getLevelContent(Long levelId, Integer next, Integer limit) {
        List<Content> contentList = contentDAO.selectContentListWithLevel(levelId, next, limit);
        List<ContentDTO> contentDTOList = new ArrayList<>();

        for (Content c: contentList) {
            // best practice? domain --> dto ...
            ContentDTO dto = ContentDTO.builder()
                    .id(c.getId())
                    .title(c.getTitle())
                    .point(c.getPoint())
                    .exercise(c.getExercise())
                    .answer(c.getAnswer())
                    .categoryChapterId(c.getCategoryChapterId())
                    .levelNum(c.getLevelNum())
                    .build();
            contentDTOList.add(dto);
        }

        return contentDTOList;
    }

    @Override
    public ContentWithPayloadDTO getContentPayload(Long contentId) {

        Content content = contentDAO.selectContentWithPayload(contentId);

        List <ContentPayloadDTO> contentPayloadDTOList = new ArrayList<>();
        for (ContentPayload c : content.getPayloadList()) {
            ContentPayloadDTO dto
                    = ContentPayloadDTO
                    .builder()
                    .contentId(c.getId().getContentId())
                    .number(c.getId().getNumber())
                    .type(c.getType())
                    .payload(c.getPayload())
                    .build();
            contentPayloadDTOList.add(dto);
        }

        ContentWithPayloadDTO contentDTO = ContentWithPayloadDTO.builder()
                .id(content.getId())
                .title(content.getTitle())
                .point(content.getPoint())
                .exercise(content.getExercise())
                .answer(content.getAnswer())
                .levelNum(content.getLevelNum())
                .categoryChapterId(content.getCategoryChapterId())
                .payloadList(contentPayloadDTOList)
                .build();

        return contentDTO;
    }

    @Transactional
    @Override
    public ContentStudiedDTO studyContent(User authUser, Long contentId) {

        ContentStudied contentStudied = ContentStudied.builder()
                .id(new ContentStudiedId(authUser.getId(), contentId))
                .user(new User(authUser.getId()))
                .content(new Content(contentId))
                .kept(false)
                .build();

        ContentStudied savedStudy = contentStudiedDAO.insertContentStudied(contentStudied);

        ContentStudiedDTO contentStudiedDTO = ContentStudiedDTO.builder()
                .userId(savedStudy.getId().getUserId())
                .contentId(savedStudy.getId().getContentId())
                .myAnswer(savedStudy.getMyAnswer())
                .kept(savedStudy.getKept())
                .createdAt(savedStudy.getCreatedAt())
                .updatedAt(savedStudy.getUpdatedAt())
                .build();


        return contentStudiedDTO;

    }

    @Override
    public ContentStudiedDTO submitExercise(User authUser, Long contentId, ContentExerciseReqDTO contentExerciseReqDTO) {
        // TODO
        // handle when content studied exist
        // : unnecessary update occurs for updatedAt
        Boolean answer = contentExerciseReqDTO.getAnswer();

        ContentStudied updatedStudy = contentStudiedDAO.updateContentStudied(authUser.getId(), contentId, answer);

        ContentStudiedDTO contentStudiedDTO = ContentStudiedDTO.builder()
                .userId(updatedStudy.getId().getUserId())
                .contentId(updatedStudy.getId().getContentId())
                .myAnswer(updatedStudy.getMyAnswer())
                .kept(updatedStudy.getKept())
                .createdAt(updatedStudy.getCreatedAt())
                .updatedAt(updatedStudy.getUpdatedAt())
                .build();

        return contentStudiedDTO;
    }

    @Override
    public List<CategoryDTO> getCategoryList() {

        List<Category> categoryList = categoryDAO.selectCategoryList();

        List<CategoryDTO> categoryDTOList = new ArrayList<>();

        for (Category level: categoryList) {
            categoryDTOList.add(new CategoryDTO(level.getId(), level.getTitle(), level.getThumbnailUrl()));
        }

        return categoryDTOList;

    }
    @Override
    public List<CategoryChapterWithContentDTO> getCategoryChapterWithContent(Long categoryId) {

        List<CategoryChapter> chapterList = categoryChapterDAO.selectCategoryChapterListWithContent(categoryId);

        // return var
        List<CategoryChapterWithContentDTO> categoryDTOList = new ArrayList<>();

        // iterate each chapter
        for (CategoryChapter ch: chapterList) {

            // prepare content dto list for each chapter
            List <ContentDTO> contentDTOList = new ArrayList<>();

            for (Content c : ch.getContentList()) {
                ContentDTO contentDTO
                        = ContentDTO
                        .builder()
                        .id(c.getId())
                        .title(c.getTitle())
                        .point(c.getPoint())
                        .exercise(c.getExercise())
                        .answer(c.getAnswer())
                        .levelNum(c.getLevelNum())
                        .categoryChapterId(c.getCategoryChapterId())
                        .build();

                contentDTOList.add(contentDTO);
            }

            CategoryChapterWithContentDTO dto
                    = CategoryChapterWithContentDTO
                        .builder()
                        .id(ch.getId())
                        .title(ch.getTitle())
                        .contentList(contentDTOList)
                        .build();

            categoryDTOList.add(dto);
        }

        return categoryDTOList;

    }
    @Override
    public ContentDTO getContentStudiedLatest(User authUser) {
        // TODO
        // handle when there's no more content left
        // decide rule for content Id
        Content content = contentDAO.selectStudiedLatest(authUser.getId());

        ContentDTO contentDTO
                = ContentDTO
                .builder()
                .id(content.getId())
                .title(content.getTitle())
                .point(content.getPoint())
                .exercise(content.getExercise())
                .answer(content.getAnswer())
                .levelNum(content.getLevelNum())
                .categoryChapterId(content.getCategoryChapterId())
                .build();

        return contentDTO;
    }

    @Override
    public List<ContentWithStudiedDTO> getContentKept(User authUser, Integer next, Integer limit) {

        List<ContentStudied> contentStudiedList
                = contentStudiedDAO.selectContentListKept(authUser.getId(), next, limit);

        List<ContentWithStudiedDTO> contentDTOList = new ArrayList<>();

        for (ContentStudied s: contentStudiedList) {

            // prepare content studied dto
            ContentStudiedDTO contentStudiedDTO
                    = ContentStudiedDTO
                    .builder()
                    .userId(s.getId().getUserId())
                    .contentId(s.getId().getContentId())
                    .myAnswer(s.getMyAnswer())
                    .kept(s.getKept())
                    .createdAt(s.getCreatedAt())
                    .updatedAt(s.getUpdatedAt())
                    .build();

            Content c = s.getContent();


            ContentWithStudiedDTO dto = ContentWithStudiedDTO.builder()
                    .id(c.getId())
                    .title(c.getTitle())
                    .point(c.getPoint())
                    .exercise(c.getExercise())
                    .answer(c.getAnswer())
                    .levelNum(c.getLevelNum())
                    .categoryChapterId(c.getCategoryChapterId())
                    .contentStudied(contentStudiedDTO)
                    .build();
            contentDTOList.add(dto);

        }

        return contentDTOList;
    }

    @Override
    public List<ContentWithStudiedDTO> getContentStudied(User authUser, Integer next, Integer limit) {

        List<ContentStudied> contentStudiedList
                = contentStudiedDAO.selectContentListStudied(authUser.getId(), next, limit);

        List<ContentWithStudiedDTO> contentDTOList = new ArrayList<>();

        for (ContentStudied s: contentStudiedList) {

            // prepare content studied dto
            ContentStudiedDTO contentStudiedDTO
                    = ContentStudiedDTO
                    .builder()
                    .userId(s.getId().getUserId())
                    .contentId(s.getId().getContentId())
                    .myAnswer(s.getMyAnswer())
                    .kept(s.getKept())
                    .createdAt(s.getCreatedAt())
                    .updatedAt(s.getUpdatedAt())
                    .build();

            Content c = s.getContent();


            ContentWithStudiedDTO dto = ContentWithStudiedDTO.builder()
                    .id(c.getId())
                    .title(c.getTitle())
                    .point(c.getPoint())
                    .exercise(c.getExercise())
                    .answer(c.getAnswer())
                    .levelNum(c.getLevelNum())
                    .categoryChapterId(c.getCategoryChapterId())
                    .contentStudied(contentStudiedDTO)
                    .build();
            contentDTOList.add(dto);

        }

        return contentDTOList;
    }

    @Override
    public ContentStudiedDTO keepContent(User authUser, Long contentId) {

        ContentStudied keptStudy = contentStudiedDAO.keepContent(authUser.getId(), contentId);

        ContentStudiedDTO contentStudiedDTO = ContentStudiedDTO.builder()
                .userId(keptStudy.getId().getUserId())
                .contentId(keptStudy.getId().getContentId())
                .myAnswer(keptStudy.getMyAnswer())
                .kept(keptStudy.getKept())
                .createdAt(keptStudy.getCreatedAt())
                .updatedAt(keptStudy.getUpdatedAt())
                .build();

        return contentStudiedDTO;
    }

}