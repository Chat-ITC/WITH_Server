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

    public void saveRefreshToken(String refreshToken,String snsId) {
        redisTemplate.opsForValue().set(refreshToken,snsId, Duration.ofDays(3));
    }

    public void deleteRefreshToken(String refreshToken) {
        redisTemplate.delete(refreshToken);
    }

    public void setAccessTokenBlackList(String accessToken) {
        redisTemplate.opsForValue().set(accessToken,"blacklist",Duration.ofHours(1));
    }

    public String getsnsIdFromRefreshToken(String refreshToken) {
        return redisTemplate.opsForValue().get(refreshToken);
    }

    public ResponseCookie createHttpOnlyCookie(String refreshToken) {
        //HTTPONLY쿠키에 RefreshToken 생성 후, 전달
        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(1))
                .build();
        return responseCookie;
    }

}
