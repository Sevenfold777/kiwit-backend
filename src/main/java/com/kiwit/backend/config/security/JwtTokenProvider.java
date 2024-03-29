package com.kiwit.backend.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Value("${jwt.secret}")
    private String SECRET_KEY_RAW;

    private Key SECRET_KEY;

    // [expires in] Access Token = 1hour, refresh Token = 15days (in milliseconds)
    private static final Long accessTokenExpires = 1000L * 60 * 60;
    private static final Long refreshTokenExpires = 1000L * 60 * 60 * 24 * 15;
    private static final String AUTH_KEY = "Authorization";
    private static final String AUTH_PREFIX = "Bearer ";

    @PostConstruct
    protected void init() {
        LOGGER.info("[init] JwtTokenProvider 내 secretKey 초기화 시작");
        // encode JWT in Base64 format
        SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_RAW.getBytes());
        LOGGER.info("[init] JwtTokenProvider 내 secretKey 초기화 완료");
    }

    public String issueToken(Long id, Boolean isRefreshToken) {
        LOGGER.info("[issueToken] Start Generating Token.");

        Claims claims = Jwts.claims().setSubject(id.toString());
        claims.put("roles", "USER");

        Date now = new Date();
        Long tokenExpires = isRefreshToken ? refreshTokenExpires : accessTokenExpires;
        Date expiresAt = new Date(now.getTime() + tokenExpires);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiresAt)
                .signWith(SECRET_KEY)
                .compact();

        LOGGER.info("[issueToken] Done Generating Token.");
        return token;
    }

    public Authentication getAuthentication(String token) {
        LOGGER.info("[getAuthentication] 토큰 인증 정보 조회 시작");

        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserId(token).toString());

        LOGGER.info("[getAuthentication] 토큰 인증 정보 조회 완료, UserDetails UserName : {}",
                this.getUserId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
    }

    public Long getUserId(String token) {

        LOGGER.info("[getUserId] 토큰 기반 회원 구별 정보 추출");

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        LOGGER.info("[getUserId] 토큰 기반 회원 구별 정보 추출 완료, info");
        return Long.parseLong(claims.getSubject());

    }

    public String resolveToken(HttpServletRequest request) {
        LOGGER.info("[resolveToken] HTTP 헤더에서 Token 값 추출");
        String token = request.getHeader(AUTH_KEY);

        // check Auth header format
        if (token == null || !token.startsWith(AUTH_PREFIX)) {
            // jwt exception handler will throw 401
            return null;
        }

        return token.substring(AUTH_PREFIX.length());
    }


    public Boolean validateToken(String token) {
        LOGGER.info("[validateToken] 토큰 유효 체크 시작");

        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);

            LOGGER.info("[validateToken] 토큰 유효 체크 완료");

            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {

            LOGGER.info("[validateToken] 토큰 유효 체크 예외 발생");
            return false;
        }
    }

}
