package codingFriends_Server.global.auth.jwt;

import codingFriends_Server.domain.Member.entity.Member;
import codingFriends_Server.domain.Member.repository.MemberRepository;
import codingFriends_Server.global.common.exception.CustomException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.util.Date;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {

    private final MemberRepository memberRepository;
    @Value("${JWT_SECRET_KEY}")
    private String JWT_SECRET_KEY;

    //    private final Long accessTokenValidTime = 1000 * 60L * 60L; // 60분
    private final Long accessTokenValidTime = 30000L;
    private final Long refreshTokenValidTime = 1000 * 60 * 60 * 24 * 7L; // 1주

    //AccessToken 생성
    public String createAccessToken(String snsId) {
        Claims claims = Jwts.claims().setSubject("accessToken");
        claims.put("snsId", snsId);
        Date currentTime = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(currentTime)
                .setExpiration(new Date(currentTime.getTime() + accessTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, Base64Utils.encodeToString(JWT_SECRET_KEY.getBytes()))
                .compact();
    }

    //RefreshToken 생성
    public String createRefreshToken(String snsId) {
        Claims claims = Jwts.claims().setSubject("refreshToken");
        claims.put("snsId", snsId);
        Date currentTime = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(currentTime)
                .setExpiration(new Date(currentTime.getTime() + refreshTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, Base64Utils.encodeToString(JWT_SECRET_KEY.getBytes()))
                .compact();
    }

    //AccessToken 유효성 검사
    public Boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Base64Utils.encodeToString(JWT_SECRET_KEY.getBytes()))
                    .build()
                    .parseClaimsJws(token).getBody();
            return true;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "토큰 Validate 과정에서 에러가 생겼습니다.");
        }
    }

    //User의 정보를 가져온다.
    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        //@AuthenticationPrincipal에서 필요한 정보 여기에 담기
        String snsId = getsnsIdFromToken(token);
        Member member = memberRepository.findBySnsId(snsId)
                .orElseThrow(() -> new RuntimeException("Member 를 찾지 못했습니다."));
        MemberPrincipal memberPrincipal = new MemberPrincipal(member);
        log.info("멤버 저장 잘 됐어요");
        log.info(memberPrincipal.toString());
        return new UsernamePasswordAuthenticationToken(memberPrincipal, token,
                member.getAuthorities());
    }

    //Token으로부터 snsId 추출
    public String getsnsIdFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Base64Utils.encodeToString(JWT_SECRET_KEY.getBytes()))
                .build().parseClaimsJws(token)
                .getBody()
                .get("snsId",
                        String.class);
    }

    //Jwt Token의 유효성 검사
    public Claims decodeJwtToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(Base64Utils.encodeToString(JWT_SECRET_KEY.getBytes())).build()
                    .parseClaimsJws(token).getBody();
            return claims;
        } catch (SecurityException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException
                 | IllegalStateException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Token Decode 과정에서 에러가 생겼습니다.");
        }
    }
}
