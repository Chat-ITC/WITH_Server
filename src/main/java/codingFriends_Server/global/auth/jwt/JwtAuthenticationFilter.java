package codingFriends_Server.global.auth.jwt;

import codingFriends_Server.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Struct;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final TokenProvider tokenProvider;
	private final RedisTemplate<String, String> redisTemplate;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String jwt = retrieveToken(request);
		log.info(jwt);
		if (StringUtils.hasText(jwt) && tokenProvider.validateAccessToken(jwt)) {
			String isLogout = (String)redisTemplate.opsForValue().get(jwt);
			if (ObjectUtils.isEmpty(isLogout)) { // AccessToken 블랙리스트 검사
				SecurityContextHolder
					.getContext()
					.setAuthentication(
						tokenProvider.getAuthentication(jwt)
					);
			}
		} else {
			log.info("유효한 토큰이 없습니다.");
		}
		filterChain.doFilter(request, response); // jwt를 가지고 있으면 SecurityContextHolder에 저장을 하고 없으면 넘어감
	}

	//HttpServletRequest로 부터 token 추출
	private String retrieveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
