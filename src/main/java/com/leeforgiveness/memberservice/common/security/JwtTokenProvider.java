package com.leeforgiveness.memberservice.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final Environment env;

    @Value("${JWT.SECRET_KEY}")
    private String secretKey;

    public String getUuid(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(env.getProperty("JWT.SECRET_KEY"));
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(Map.of(), userDetails);
    }

    public String generateToken(Map<String, Object> extractClaims, UserDetails userDetails) {
        log.info("generateToken {}", userDetails);
        return Jwts.builder()
                .setClaims(extractClaims) //정보저장
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis())) //토근 발행 시간
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
