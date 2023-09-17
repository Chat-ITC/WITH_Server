package codingFriends_Server.global.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final RedisTemplate<String, String> redisTemplate;

    public void saveRefreshToken(String snsId, String refreshToken) {
        redisTemplate.opsForValue().set(snsId,refreshToken, Duration.ofDays(3));
    }

    public void deleteRefreshToken(String snsId) {
        redisTemplate.delete(snsId);
    }

    public void setAccessTokenBlackList(String accessToken) {
        redisTemplate.opsForValue().set(accessToken,"blacklist",Duration.ofHours(1));
    }

    public Boolean getRefreshToken(String snsId) {
        return redisTemplate.hasKey(snsId);
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
