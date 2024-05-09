//package com.leeforgiveness.memberservice.common.security;
//
//import static com.leeforgiveness.memberservice.common.exception.BaseResponseStatus.JWT_VALID_FAILED;
//
//import com.leeforgiveness.memberservice.common.exception.BaseException;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//
//import java.security.Key;
//
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.env.Environment;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import java.util.Date;
//import java.util.Map;
//import java.util.function.Function;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class JwtTokenProvider {
//
//    private final Environment env;
//
//    @Value("${JWT.SECRET_KEY}")
//    private String secretKey;
//
//    public String getUuid(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    private Key getSigningKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(env.getProperty("JWT.SECRET_KEY"));
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts
//                .parserBuilder()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    public String generateToken(UserDetails userDetails) {
//        return generateToken(Map.of(), userDetails);
//    }
//
//    public String generateToken(Map<String, Object> extractClaims, UserDetails userDetails) {
//        log.info("generateToken {}", userDetails);
//        return Jwts.builder()
//                .setClaims(extractClaims) //정보저장
//                .setSubject(userDetails.getUsername())
//                .setIssuedAt(new Date(System.currentTimeMillis())) //토근 발행 시간
//                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public String validateAndGetUserUuid(String token) {
//        try {
//            return extractClaim(token, Claims::getSubject);
//        } catch (Exception e) {
//            throw new BaseException(JWT_VALID_FAILED);
//        }
//    }
//
//    public boolean validate(String token, UserDetails userDetails) {
//        final String uuid = getUuid(token);
//        return uuid.equals(userDetails.getUsername());
//    }
//
//    public String getHeader() {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//        String headerValue = request.getHeader("Authorization");
//        if (headerValue != null && headerValue.startsWith("Bearer ")) {
//            return headerValue.substring(7).trim();
//        }
//        throw new IllegalArgumentException("토큰이 없습니다.");
//    }
//}
