package codingFriends_Server.global.auth.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final RedisTemplate<String, String> redisTemplate;

	public void saveRefreshToken(String refreshToken, String snsId) { // redis에 refreshToken 저장
		redisTemplate.opsForValue().set(refreshToken, snsId, Duration.ofDays(3));
	}

	public void deleteRefreshToken(String refreshToken) {
		redisTemplate.delete(refreshToken);
	}

	public void setAccessTokenBlackList(String accessToken) { // 로그아웃 할 때 AccessToken을 블랙리스트 처리 함
		redisTemplate.opsForValue().set(accessToken, "blacklist", Duration.ofHours(1));
	}

	public String getsnsIdFromRefreshToken(String refreshToken) { // RefreshToken으로부터 snsId를 받는다.
		return redisTemplate.opsForValue().get(refreshToken);
	}

	public ResponseCookie createHttpOnlyCookie(
		String refreshToken) { // Client에서 접근 못 하는 HttpOnlyCookie에 RefreshToken 저장
		//HTTPONLY쿠키에 RefreshToken 생성 후, 전달
		ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refreshToken)
			.httpOnly(true)
			.secure(true) // secure을 True로 하면 Https에서만 실행 가능함
			.path("/")
			.maxAge(Duration.ofDays(1))
			.build();
		return responseCookie;
	}

}
