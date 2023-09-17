package codingFriends_Server.global.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
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

}
