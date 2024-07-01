package com.kiwit.backend.service.impl;

import com.kiwit.backend.config.security.JwtTokenProvider;
import com.kiwit.backend.domain.*;
import com.kiwit.backend.domain.compositeKey.ContentStudiedId;
import com.kiwit.backend.dto.*;
import com.kiwit.backend.service.ContentService;
import com.kiwit.backend.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.parameters.P;

import java.util.List;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
@DisplayName("ContentService Test")
class ContentServiceImplTest {

    private final ContentService contentService;
    private final ServiceTestHelper serviceTestHelper;
    private final EntityManager em;

    /* fields for global entities
       - list var name ends with "s" not "List" */
    private Long userId;
    private List<Level> levels;
    private List<Category> categories;
    private List<CategoryChapter> categoryChapters;
    private List<Content> contents;

    @Autowired
    public ContentServiceImplTest(
            EntityManager em,
            ContentService contentService,
            UserService userService,
            JwtTokenProvider jwtTokenProvider) {
        this.contentService = contentService;

        this.em = em;
        this.serviceTestHelper = new ServiceTestHelper(
                this.em, userService, jwtTokenProvider
        );
    }


    @Test
    @DisplayName("레벨 리스트 조회")
    public void getLevelListTest() {

        // Given
        List<Level> levels = serviceTestHelper.createLevels();

        // When
        List<LevelDTO> levelList = contentService.getLevelList();

        // Then
        assertThat(levelList.size()).isEqualTo(levels.size());
        assertThat(levelList.stream().map(l -> l.getNum()).collect(toList()))
                .isEqualTo(levels.stream().map(l -> l.getNum()).collect(toList()));
    }

    @Test
    @DisplayName("카테고리 목록 조회")
    public void getCategoryListTest() {

        // Given
        List<Category> categories = serviceTestHelper.createCategories();

        // When
        List<CategoryDTO> categoryList = contentService.getCategoryList();

        // Then
        assertThat(categoryList.size()).isEqualTo(categories.size());
        assertThat(categoryList.stream().map(CategoryDTO::getId).collect(toList()))
                .isEqualTo(categories.stream().map(Category::getId).collect(toList()));

    }

    @Test
    @DisplayName("레벨 내 콘텐츠 조회 성공")
    public void getLevelContentsSuccessTest() {

        // Given
        initContents(); // lv.1~5
        List<Content> level1Contents = contents.stream().filter(
                c -> c.getLevelNum().equals(1L)).toList();

        // When
        List<ContentDTO> contentDTOList = contentService.getLevelContent(1L, 0, 20);

        // Then
        assertThat(level1Contents.stream().map(c -> c.getId()).collect(toList()))
                .isEqualTo(contentDTOList.stream().map(c -> c.getId()).collect(toList()));
    }

    @Test
    @DisplayName("존재하지 않는 레벨 조회")
    public void getNonExistingLevelTest() {

        // Given
        initContents(); // lv.1~5

        // When
        List<ContentDTO> contentDTOList = contentService.getLevelContent(999L, 0, 20);
        assertThat(contentDTOList.size()).isEqualTo(0);
    }


    @Test
    @DisplayName("카테고리 챕터 조회 (with Contents)")
    public void getChapterWithContestsTest() {

        // Given
        initContents();

        Long tgtCategoryId = categories.get(0).getId();
        List<CategoryChapter> categoryChaptersId1 = categoryChapters.stream()
                .filter(c -> c.getCategory().getId().equals(tgtCategoryId)).toList();

        // When
        List<CategoryChapterWithContentDTO> categoryList
                = contentService.getCategoryChapterWithContent(tgtCategoryId);

        // Then
        assertThat(categoryList.stream().map(c -> c.getId()).collect(toList()))
                .isEqualTo(categoryChaptersId1.stream().map(c -> c.getId()).collect(toList()));
    }

    @Test
    @DisplayName("콘텐츠 내용 조회, 학습 전")
    public void getContentPayloadTest() {

        // Given
        initUser();
        User user = new User(userId);

        initContents();
        Content content = contents.get(0);

        // When
        ContentWithStudiedDTO contentWithStudiedDTO = contentService.getContentPayload(user, content.getId());

        // Then
        assertThat(contentWithStudiedDTO.getPayloadUrl())
                .isEqualTo(content.getPayloadUrl());

        assertThat(contentWithStudiedDTO.getContentStudied()).isNull();
    }

    @Test
    @DisplayName("콘텐츠 내용 조회, 이미 학습한 콘텐츠")
    public void getContentPayloadAlreadyStudied() {

        // given
        initUser();
        initContents();

        User user = em.find(User.class, userId);
        Content content = em.find(Content.class, contents.get(0).getId());

        // content studied 생성 => 이미 학습 완료 한 콘텐츠
        ContentStudied contentStudied = new ContentStudied(
                new ContentStudiedId(userId, content.getId()),true, false, user, content
        );

        em.persist(contentStudied);

        em.flush();
        em.clear();


        // when
        ContentWithStudiedDTO contentWithStudiedDTO = contentService.getContentPayload(user, content.getId());

        // then
        assertThat(contentWithStudiedDTO.getPayloadUrl())
                .isEqualTo(content.getPayloadUrl());

        assertThat(contentWithStudiedDTO.getContentStudied().getUserId())
                .isEqualTo(userId);

        assertThat(contentWithStudiedDTO.getContentStudied().getContentId())
                .isEqualTo(content.getId());
    }

    @Test
    @DisplayName("콘텐츠 내용 조회 실패")
    public void getContentPayloadFailTest() {

        // Given
        initUser();
        User user = new User(userId);

        initContents();

        // When
        // 처음 DB 초기화 시 조회 성공 => 테스트 재실행시 정상 동작
        assertThrows(DataAccessException.class,
                () -> contentService.getContentPayload(user, 1L));
    }

    @Test
    @DisplayName("학습 완료 테스트")
    public void studiedContentTest() {

        // Given
        initContents();
        Content content = contents.get(0);

        initUser();
        User user = new User(userId);

        // When
        ContentStudiedDTO contentStudiedDTO = contentService.studiedContent(user, content.getId());

        // Then
        ContentDTO contentStudiedLatest = contentService.getContentStudiedLatest(user);

        assertThat(contentStudiedDTO.getContentId())
                .isEqualTo(contentStudiedLatest.getId());
    }

    @Test
    @DisplayName("최근 학습 없을 때 조회 테스트")
    public void getStudiedLatestFailTest() {

        // Given
        initContents();
        Content content = contents.get(0);

        initUser();
        User user = new User(userId);

        // When
        assertThrows(DataAccessException.class,
                () -> contentService.getContentStudiedLatest(user));
    }

    @Test
    @DisplayName("예제 풀기 테스트")
    public void submitExerciseTest() {

        // Given
        initContents();
        Content content = contents.get(0);

        initUser();
        User user = new User(userId);

        ContentExerciseReqDTO contentExerciseReqDTO = new ContentExerciseReqDTO(true);

        // When
        ContentStudiedDTO contentStudiedDTO = contentService.studiedContent(user, content.getId());
        ContentStudiedDTO exerciseResultDTO = contentService.submitExercise(user, content.getId(), contentExerciseReqDTO);

        // Then
        assertThat(contentStudiedDTO.getMyAnswer()).isNull();
        assertThat(exerciseResultDTO.getMyAnswer()).isEqualTo(contentExerciseReqDTO.getAnswer());
    }

    @Test
    @DisplayName("보관한 콘텐츠 조회")
    public void getContentsKeptTest() {

        // Given
        initContents();
        Content content = contents.get(0);

        initUser();
        User user = new User(userId);

        // When
        ContentStudiedDTO contentStudiedDTO = contentService.studiedContent(user, content.getId());
        ContentStudiedDTO keepResultDTO = contentService.keepContent(user, content.getId());
        List<ContentWithStudiedDTO> contentKeptList = contentService.getContentKept(user, 0, 20);

        // Then
        ContentStudiedDTO tgtContentStudied = contentKeptList.get(0).getContentStudied();

        assertThat(keepResultDTO.getKept()).isEqualTo(tgtContentStudied.getKept());
        assertThat(keepResultDTO.getContentId()).isEqualTo(tgtContentStudied.getContentId());
    }

    @Test
    @DisplayName("보관한 콘텐츠 조회 (보관 내역 없음)")
    public void getContentsKeptNoneTest() {

        // Given
        initContents();
        Content content = contents.get(0);

        initUser();
        User user = new User(userId);

        // When
        List<ContentWithStudiedDTO> contentKeptList = contentService.getContentKept(user, 0, 20);

        // Then
        assertThat(contentKeptList.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("학습한 콘텐츠 조회")
    public void getStudiedContentTest() {

        // Given
        initContents();
        Content content1 = contents.get(0);
        Content content2 = contents.get(1);
        Content content3 = contents.get(2);

        initUser();
        User user = new User(userId);

        // When
        ContentStudiedDTO contentStudiedDTO1 = contentService.studiedContent(user, content1.getId());
        ContentStudiedDTO contentStudiedDTO2 = contentService.studiedContent(user, content2.getId());
        ContentStudiedDTO contentStudiedDTO3 = contentService.studiedContent(user, content3.getId());

        List<ContentWithStudiedDTO> contentStudiedList = contentService.getContentStudied(user, 0, 20);

        // Then
        assertThat(contentStudiedDTO1.getContentId()).isEqualTo(contentStudiedList.get(2).getId());
        assertThat(contentStudiedDTO2.getContentId()).isEqualTo(contentStudiedList.get(1).getId());
        assertThat(contentStudiedDTO3.getContentId()).isEqualTo(contentStudiedList.get(0).getId());

    }

    @Test
    @DisplayName("학습한 콘텐츠 조회 (학습 내역 없음)")
    public void getStudiedContentNoneTest() {

        // Given
        initContents();

        initUser();
        User user = new User(userId);

        // When
        List<ContentWithStudiedDTO> contentStudiedList = contentService.getContentStudied(user, 0, 20);

        // Then
        assertThat(contentStudiedList.size()).isEqualTo(0);
    }



    /* Database Initializers */

    private void initUser() {
        userId = serviceTestHelper.getNewUserId();

        em.flush();
        em.clear();
    }

    private void initContents() {
        levels = serviceTestHelper.createLevels(); // init Level
        categories = serviceTestHelper.createCategories(); // init Category
        categoryChapters = serviceTestHelper.createCategoryChapters(categories); // init Chapter
        contents = serviceTestHelper.createContentWithPayload(categoryChapters, levels); // init Contents & Payload

        em.flush();
        em.clear();
    }

}