//package com.leeforgiveness.memberservice.common.security;
//
//import static com.leeforgiveness.memberservice.common.exception.ResponseStatus.LOGOUT_TOKEN;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.leeforgiveness.memberservice.common.CommonResponse;
//import com.leeforgiveness.memberservice.common.exception.ExceptionResponse;
//import com.leeforgiveness.memberservice.common.exception.ResponseStatus;
//import com.leeforgiveness.memberservice.common.redis.RedisUtils;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import lombok.NonNull;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//	private final JwtTokenProvider jwtTokenProvider;
//	private final UserDetailsService userDetailsService;
//	private final RedisUtils redisUtils;
//
//	@Override
//	protected void doFilterInternal(
//		@NonNull
//		HttpServletRequest request,
//		@NonNull
//		HttpServletResponse response,
//		@NonNull
//		FilterChain filterChain
//	) throws ServletException, IOException {
//
//		try {
//			final String authHeader = request.getHeader("Authorization");
//			final String jwt;
//			final String userUuid;
//
//			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//				filterChain.doFilter(request, response);
//				return;
//			}
//
//			jwt = authHeader.substring(7);
//			userUuid = jwtTokenProvider.validateAndGetUserUuid(jwt);
//
//			validBlackToken(jwt);
//
//			if (SecurityContextHolder.getContext().getAuthentication() == null) {
//				UserDetails userDetails = this.userDetailsService.loadUserByUsername(userUuid);
//				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//					userDetails,
//					null,
//					userDetails.getAuthorities()
//				);
//				authenticationToken.setDetails(
//					new WebAuthenticationDetailsSource().buildDetails(request));
//				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//			}
//
//			filterChain.doFilter(request, response);
//
//		} catch (FailedException e) {
//			ExceptionResponse exceptionResponse = e.getResponse();
//			response.setStatus(exceptionResponse.getStatusCode().value());
//			response.setContentType("application/json");
//			response.setCharacterEncoding("utf-8");
//			ObjectMapper objectMapper = new ObjectMapper();
//			String jsonErrorResponse = objectMapper.writeValueAsString(
//				CommonResponse.fail(ResponseStatus.valueOf(exceptionResponse.getStatusCode().value(), exceptionResponse.getBody())));
//
//			response.getWriter().write(jsonErrorResponse);
//		}
//
//	}
//
//	private void validBlackToken(String accessToken) {
//		// Redis에 있는 엑세스 토큰인 경우 로그아웃 처리된 엑세스 토큰.
//		String blackToken = redisUtils.getData(accessToken);
//		if (StringUtils.hasText(blackToken)) {
//			throw new FailedException(new ExceptionResponse(LOGOUT_TOKEN));
//		}
//	}
//}
