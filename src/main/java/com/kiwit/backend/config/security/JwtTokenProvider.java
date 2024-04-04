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
        // encode JWT in Base64 format
        SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_RAW.getBytes());
    }

    public String issueToken(Long id, Boolean isRefreshToken) {

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

        return token;
    }

    public Authentication getAuthentication(String token) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserId(token).toString());

        return new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
    }

    public Long getUserId(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(AUTH_KEY);

        // check Auth header format
        if (token == null || !token.startsWith(AUTH_PREFIX)) {
            // jwt exception handler will throw 401
            return null;
        }

        return token.substring(AUTH_PREFIX.length());
    }


    public Boolean validateToken(String token) {

        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}
