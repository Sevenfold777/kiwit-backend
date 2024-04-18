package com.kiwit.backend.service.impl;

import com.kiwit.backend.common.constant.Provider;
import com.kiwit.backend.common.constant.Status;
import com.kiwit.backend.config.security.JwtTokenProvider;
import com.kiwit.backend.domain.Trophy;
import com.kiwit.backend.domain.TrophyAwarded;
import com.kiwit.backend.domain.User;
import com.kiwit.backend.domain.UserInfo;
import com.kiwit.backend.domain.compositeKey.TrophyAwardedId;
import com.kiwit.backend.dto.*;
import com.kiwit.backend.repository.TrophyAwardedRepository;
import com.kiwit.backend.repository.TrophyRepository;
import com.kiwit.backend.repository.UserInfoRepository;
import com.kiwit.backend.repository.UserRepository;
import com.kiwit.backend.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("UserService Test")
class UserServiceImplTest {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;
    @Autowired UserInfoRepository userInfoRepository;
    @Autowired TrophyRepository trophyRepository;
    @Autowired TrophyAwardedRepository trophyAwardedRepository;
    @Autowired JwtTokenProvider jwtTokenProvider;

    @PersistenceContext
    EntityManager em;

    private final String trophyName1 = "첫 번째 트로피";
    private final String trophyName2 = "두 번째 트로피";

    @Test
    @DisplayName("회원가입 성공")
    public void signUpSuccessTest() throws Exception {

        // Given
        String email = "spring@gmail.com";
        String nickname = "signUpTest";
        SignUpReqDTO signUpReqDTO = new SignUpReqDTO(email, nickname, Provider.KAKAO);

        // When
        SignInResDTO signInResDTO = userService.signUp(signUpReqDTO); // sign-up
        Long userId = jwtTokenProvider.getUserId(signInResDTO.getAccessToken()); // get userId from jwt

        // Then
        UserDTO userDTO = userService.myProfile(new User(userId)); // myProfile
        assertEquals(email, userDTO.getEmail());

        // check if
        UserInfo userInfo = userInfoRepository.findById(userDTO.getId())
                .orElseThrow();
        assertFalse(userInfo.getJwtRefreshToken().isBlank());
    }

    @Test
    @DisplayName("중복회원 가입")
    public void signUpDuplicateTest() throws Exception {

        // Given
        List<Map<String, String>> userDTOs = new ArrayList<>() {{
            add(new HashMap<>(){{
                put("email", "spring@gmail.com");
                put("nickname", "signUpTest");
            }});
            add(new HashMap<>(){{
                put("email", "spring_1@gmail.com");
                put("nickname", "signUpTest");
            }});
            add(new HashMap<>(){{
                put("email", "spring@gmail.com");
                put("nickname", "signUpTest_1");
            }});
        }};


        SignUpReqDTO signUpReqDTO = new SignUpReqDTO(
                userDTOs.get(0).get("email"),
                userDTOs.get(0).get("nickname"),
                Provider.KAKAO);

        // When
        SignInResDTO signInResDTO = userService.signUp(signUpReqDTO); // sign-up

        // Then
        SignUpReqDTO signUpReqDTO1 = new SignUpReqDTO(
                userDTOs.get(1).get("email"),
                userDTOs.get(1).get("nickname"),
                Provider.KAKAO);

        SignUpReqDTO signUpReqDTO2 = new SignUpReqDTO(
                userDTOs.get(2).get("email"),
                userDTOs.get(2).get("nickname"),
                Provider.KAKAO);

        assertThrows(DataAccessException.class, () -> userService.signUp(signUpReqDTO));
        assertThrows(DataAccessException.class, () -> userService.signUp(signUpReqDTO1));
        assertThrows(DataAccessException.class, () -> userService.signUp(signUpReqDTO2));
    }

    @Test
    @DisplayName("로그아웃")
    public void signOutSuccessTest() throws Exception {

        // Given
        Long userId = this.getNewUserId();
        UserInfo userInfo1 = userInfoRepository.findById(userId).orElseThrow();
        assertFalse(userInfo1.getJwtRefreshToken() == null || userInfo1.getJwtRefreshToken().isBlank());

        // When
        userService.signOut(new User(userId));

        // Then
        UserInfo userInfo2 = userInfoRepository.findById(userId).orElseThrow();
        assertNull(userInfo2.getJwtRefreshToken());
    }

    @Test
    @DisplayName("사용자 정보 수정 성공")
    public void editUserSuccessTest() throws Exception {

        // Given
        Long userId = this.getNewUserId();
        EditUserReqDTO editUserReqDTO = new EditUserReqDTO("Modified");

        // When
        UserDTO userDTO = userService.editUser(new User(userId), editUserReqDTO);

        // Then
        assertEquals(editUserReqDTO.getNickname(), userDTO.getNickname());
    }

    @Test
    @DisplayName("사용자 정보 수정 중복")
    public void editUserDuplicateTest() throws Exception {

        // Given
        Long userId1 = this.getNewUserId("aaa@gmail.com", "aaa", Provider.KAKAO);
        Long userId2 = this.getNewUserId("bbb@gmail.com", "bbb", Provider.KAKAO);
        EditUserReqDTO editUserReqDTO = new EditUserReqDTO("bbb");

        // When
        userService.editUser(new User(userId1), editUserReqDTO);

        // Then
        assertThrows(ConstraintViolationException.class,
                () -> em.flush());
    }

    @Test
    @DisplayName("회원탈퇴")
    public void withDrawUserTest() throws Exception {

        // Given
        Long userId = this.getNewUserId();

        /*
         * flush new user created + clear:
         * 뒤에서 userService.findUserWithInfo와 같은 형태로 user join 해서 받아오기 위하여
         */
        em.flush();
        em.clear(); // detach 대체 불가?
        // User a = em.find(User.class, userId);
        // em.detach(a); // detach 사용 불가: detached entity passed to persist

        User user = userRepository.findUserWithInfo(userId).orElseThrow(); // from userService.withdrawUser
        assertNotNull(user.getUserInfo().getJwtRefreshToken());
        assertEquals(Status.ACTIVATED, user.getStatus());

        // When
        userService.withdrawUser(user);

        // Then
        assertNull(user.getUserInfo().getJwtRefreshToken());
        assertEquals(Status.DEACTIVATED, user.getStatus());
    }

    @Test
    @DisplayName("JWT Refresh")
    public void refreshSuccessTest() {

        // Given
        SignInResDTO token = this.getNewUserToken();

        // When
        SignInResDTO signInResDTO = userService.refreshToken(new RefreshTokenDTO(token.getRefreshToken()));

        // Then
        assertFalse(signInResDTO.getAccessToken().isBlank());
        assertFalse(signInResDTO.getRefreshToken().isBlank());
    }

    @Test
    @DisplayName("트로피 리스트")
    public void myTrophyListTest() {

        // Given
        Long userId = this.getNewUserId();
        List<TrophyAwarded> trophyAwardedList = this.createNewTrophyAwarded(userId);

        // When
        List<TrophyAwardedDTO> myTrophyList = userService.getMyTrophyList(new User(userId), 0, 10);

        // Then
        assertEquals(2, myTrophyList.size()); // 전체 길이
        assertEquals(trophyAwardedList.get(1).getTrophy().getTitle(),
                myTrophyList.get(0).getTrophy().getTitle());
        assertEquals(trophyAwardedList.get(0).getTrophy().getTitle(),
                myTrophyList.get(1).getTrophy().getTitle());
    }

    @Test
    @DisplayName("트로피 리스트 페이지네이션")
    public void myTrophyListPageTest() {

        // Given
        Long userId = this.getNewUserId();
        List<TrophyAwarded> trophyAwardedList = this.createNewTrophyAwarded(userId);

        // When
        List<TrophyAwardedDTO> myTrophyList = userService.getMyTrophyList(new User(userId), 1, 1);

        // Then
        assertEquals(1, myTrophyList.size()); // 전체 길이
        assertEquals(trophyAwardedList.get(0).getTrophy().getTitle(),
                myTrophyList.get(0).getTrophy().getTitle());
    }

    @Test
    @DisplayName("최근 트로피")
    public void myTrophyLatest() {

        // Given
        Long userId = this.getNewUserId();
        List<TrophyAwarded> trophyAwardedList = this.createNewTrophyAwarded(userId);

        // When
        TrophyAwardedDTO trophyAwardedDTO = userService.getMyTrophyLatest(new User(userId));

        // Then
        assertEquals(trophyAwardedList.get(1).getTrophy().getTitle(),
                trophyAwardedDTO.getTrophy().getTitle());
    }


    public SignInResDTO getNewUserToken() {
        // Given
        String email = "spring@gmail.com";
        String nickname = "signUpTest";
        SignUpReqDTO signUpReqDTO = new SignUpReqDTO(email, nickname, Provider.KAKAO);

        // When
        return userService.signUp(signUpReqDTO);
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

    public List<TrophyAwarded> createNewTrophyAwarded(Long userId) {

        // Given
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

}