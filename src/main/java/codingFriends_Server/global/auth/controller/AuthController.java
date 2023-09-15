package codingFriends_Server.global.auth.controller;

import codingFriends_Server.domain.Member.entity.Member;
import codingFriends_Server.domain.Member.service.MemberService;
import codingFriends_Server.global.auth.dto.request.SignupRequestDto;
import codingFriends_Server.global.auth.dto.response.OauthResponseDto;
import codingFriends_Server.global.auth.dto.response.SignupResponseDto;
import codingFriends_Server.global.auth.dto.response.TokenResponseDto;
import codingFriends_Server.global.auth.jwt.TokenProvider;
import codingFriends_Server.global.auth.oauth.LoginProvider;
import codingFriends_Server.global.auth.oauth.kakao.KakaoLoginBO;
import codingFriends_Server.global.auth.oauth.naver.NaverLoginBO;
import com.github.scribejava.core.model.OAuth2AccessToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final NaverLoginBO naverLoginBO;
    private final KakaoLoginBO kakaoLoginBO;
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @RequestMapping(value = "/naver/callback", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<SignupResponseDto> naverLogin(@RequestParam String code, @RequestParam String state) throws IOException {
        OAuth2AccessToken oAuth2AccessToken;
        oAuth2AccessToken = naverLoginBO.getAccessToken(code, state);
        OauthResponseDto responseDto = naverLoginBO.getUserProfile(oAuth2AccessToken);
        SignupResponseDto signupResponseDto = new SignupResponseDto(responseDto, LoginProvider.NAVER);

        Optional<Member> memberOptional = memberService.findMemberBySnsId(responseDto.getSnsId()); // 추가 회원가입 과정
        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(signupResponseDto);
        }
        String accessToken = tokenProvider.createAccessToken(signupResponseDto.getSnsId());
        String refreshToken = tokenProvider.createRefreshToken(signupResponseDto.getSnsId());
        return ResponseEntity.ok()
                .header("accessToken", accessToken)
                .header("refreshToken", refreshToken)
                .body(signupResponseDto);
    }

    @RequestMapping(value = "/kakao/callback", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<SignupResponseDto> kakaoLogin(@RequestParam String code, @RequestParam String state) throws IOException {
        OAuth2AccessToken oAuth2AccessToken;
        oAuth2AccessToken = kakaoLoginBO.getAccessToken(code);
        OauthResponseDto responseDto = kakaoLoginBO.getUserProfile(oAuth2AccessToken);
        SignupResponseDto signupResponseDto = new SignupResponseDto(responseDto, LoginProvider.KAKAO);

        Optional<Member> memberOptional = memberService.findMemberBySnsId(responseDto.getSnsId()); // 추가 회원가입 과정
        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(signupResponseDto);
        }
        String accessToken = tokenProvider.createAccessToken(signupResponseDto.getSnsId());
        String refreshToken = tokenProvider.createRefreshToken(signupResponseDto.getSnsId());
        return ResponseEntity.ok()
                .header("accessToken", accessToken)
                .header("refreshToken", refreshToken)
                .body(signupResponseDto);
    }

    @PostMapping("/auth/signup")
    ResponseEntity<String> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        memberService.signup(signupRequestDto);
        return ResponseEntity.ok()
                .body("추가 회원가입 성공");
    }
}
