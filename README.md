# 🥝 kiwIT Backend


모두에게 필요한 기초 IT 지식부터 전공자를 위한 CS까지, IT 교육 서비스 kiwIT(키윗)의 백엔드 리포지토리입니다. 개발 중인 프로젝트로 지속적 커밋 예정입니다.

## 사용 기술
 

- Spring Boot
- Mysql
- AWS: EC2, ELB, RDS, Route53, ECR, S3, etc 
- Docker
- JWT Refresh Token Rotation
- 소셜 로그인 (KAKAO, APPLE 예정)

## Architecture


- 백엔드의 모든 서비스는 AWS를 통해 호스팅
- Local 개발 환경에서 RDS에 접근 시 SSH Tunneling 적용 (pem key 필요)
- Local 개발 환경에서 API run 필요시 ECR에서 이미지 다운 (IAM 권한 부여)

![architecture](https://github.com/Sevenfold777/kiwit-backend/assets/88102203/ed1be5df-e67a-42c0-929a-92b5f9c23b98)



## ERD


- v. 240314 
- Relation Edge는 Many와 One으로만 구분 (Many or none 등 무의미, 수정 예정) 

![240317](https://github.com/Sevenfold777/kiwit-backend/assets/88102203/d9cfc977-a94c-4767-9fae-75ee225c50cb)

## DB Manual Settings


### Index Ordering for Composite Keys

- 서비스 특성 상 복합키 사용시 검색 효율 상승
- kiwIT은 사용자 가준 검색 많기에 필요시 JPA default 대신 이에 맞는 index 순서 설정

```
// index 확인
SHOW INDEX FROM quiz_solved

// index 설정: Cardinality 높고 검색 빈도 높은 컬럼 우선
CREATE INDEX IDX_QUIZ_SOLVED ON quiz_solved 
(user_id, quiz_id);
```

### Event Scheduler

- status = DEACTIVATED 50일 후 >> status = DELETED 
```
CREATE EVENT soft_del_user
ON SCHEDULE EVERY 1 DAY
ON COMPLETION PRESERVE
-- STARTS '2024-03-01 03:00:00'
DO 
UPDATE db.user
SET status = 'DELETED'
WHERE status = 'DEACTIVATED'
AND updated_at < DATE_SUB(NOW(), INTERVAL 50 DAY);
```

- status = DELETED 50일 후 >> hard delete user
``` 
CREATE EVENT hard_del_user
ON SCHEDULE EVERY 1 DAY
ON COMPLETION PRESERVE
-- STARTS '2024-03-01 04:00:00'
DO 
DELETE FROM db.user
WHERE status = 'DELETED'
AND updated_at < DATE_SUB(NOW(), INTERVAL 50 DAY);
```

## Auth
 

### 가입 회원 인증 정책 (Mobile SDK 사용)
- 모바일 SDK가 1~4 단계를 일괄 처리하므로 Kakao에서 kiwIT 서버로 redirect 불가
- kakao access token을 앱에서 서버로 직접 전송 (https 적용, request body에 담아 전송)

![auth_1](https://github.com/Sevenfold777/kiwit-backend/assets/88102203/098f9913-950c-43d0-b625-1fc46e1ad010)

### 미가입자 인증 정책
- 가입 회원과 1~7 단계 동일, 소셜로그인을 통해 받은 사용자 정보가 DB에 존재하지 않으면 가입 절차 진행 
- 가입 필요 시 사용자 정보와 함께 status code 202 응답
- 온보딩 화면에서 닉네임 수정, 약관 동의 후 가입 요청 (*email 수정 불가)

![auth_4](https://github.com/Sevenfold777/kiwit-backend/assets/88102203/a2589e60-cdb9-4a8d-b05c-d1da87ffc06e)

### 토큰 재발급 정책
- 앱 단에서 access token expire 판단, refresh token을 body에 담아 재발급 요청
- 성공 시 token 발급, refresh token rotate (user_info db 저장)
- 실패 시 UNAUTHORIZED 401 응답, 앱에 토큰 삭제 요청

![auth_3](https://github.com/Sevenfold777/kiwit-backend/assets/88102203/8227c43a-bac9-4c25-9b9d-8769a57e9fe7)



### [예비] 향후 인증 정책 수정 고려 (Webview 사용)
- 소셜로그인 화면만 Webview로 구현해 kakao access token 노출을 최대로 방지, OAuth 2.0 더욱 알맞게 구현 가능 
- 현행 정책과 달리 서버와 kakao의 통신 비중 높음
- 앱 개발팀과 논의 예정

![auth_2](https://github.com/Sevenfold777/kiwit-backend/assets/88102203/f01eaa07-fbb9-4313-90dc-413a93fac567)


## ENV


❗️애플리케이션 작동을 위한 필수 환경변수 목록입니다. (로컬 개발 환경에 맞게 값 할당) <br/>
❗️Docker 이미지 내려 받아 실행 시에도 값을 할당해줘야 작동
- DB_HOST
- DB_NAME
- DB_PASSWORD
- DB_PORT
- DB_USERNAME
- JWT_SECRET_KEY

## Project Directories


```
gradle/wrapper
.gitignore
README.md
build.gradle
Dockerfile
Dockerfile.aws.json
gradlew
gradlew.bat
settings.gradle
src
│─main
│   |─resources
│   └─java/com.kiwit.backend
│       │
│       │────common
│       │      │─constant
│       │      └─exception
│       │
│       │────constant
│       │      └─security
│       │
│       │────controller
│       │
│       │────dao
│       │      
│       │────domain
│       │      
│       │────dto
│       │      └─kakao
│       │ 
│       │────repository
│       │
│       │────service
│       │
│       └─BackendApplication
│
└─test
    └─java/com.kiwit.backend
        │
        └─to be added ...
```


## Dependencies


```
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.springframework.boot:spring-boot-starter-security'
implementation 'org.springframework.boot:spring-boot-starter-web'
implementation 'org.springframework.boot:spring-boot-starter-validation'
implementation 'org.springframework.boot:spring-boot-starter-webflux'
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0'
implementation 'io.jsonwebtoken:jjwt-api:0.11.5'
implementation 'io.jsonwebtoken:jjwt-impl:0.11.5'
implementation 'io.jsonwebtoken:jjwt-jackson:0.11.5'
compileOnly 'org.projectlombok:lombok'
runtimeOnly 'com.mysql:mysql-connector-j'
annotationProcessor 'org.springframework.boot:spring-boot-configuration-processor'
annotationProcessor 'org.projectlombok:lombok'
testImplementation 'org.springframework.boot:spring-boot-starter-test'
testImplementation 'org.springframework.security:spring-security-test'

// for Apple Silicon Webclient
implementation 'io.netty:netty-resolver-dns-native-macos:4.1.68.Final:osx-aarch_64'
```

## TODO


- [ ] Pagination
- [x] Exception handling 세분화
- [ ] Test 작성
- [x] annotation 정리
- [ ] Logging Exceptions (stack trace)