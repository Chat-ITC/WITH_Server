package codingFriends_Server.global.auth.controller;

import codingFriends_Server.domain.Member.entity.Member;
import codingFriends_Server.domain.Member.service.MemberService;
import codingFriends_Server.global.auth.dto.request.SignupRequestDto;
import codingFriends_Server.global.auth.dto.response.OauthResponseDto;
import codingFriends_Server.global.auth.dto.response.SignupResponseDto;
import codingFriends_Server.global.auth.jwt.TokenProvider;
import codingFriends_Server.global.auth.oauth.LoginProvider;
import codingFriends_Server.global.auth.oauth.kakao.KakaoLoginBO;
import codingFriends_Server.global.auth.oauth.naver.NaverLoginBO;
import codingFriends_Server.global.auth.service.AuthService;
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
    private final AuthService authService;


     //OAuth는 회원가입과 로그인이 동일해서 추가 회원가입만 고려하면 됨

    @RequestMapping(value = "/naver/callback", method = {RequestMethod.GET, RequestMethod.POST}) // 네이버 로그인
    public ResponseEntity<SignupResponseDto> naverLogin(@RequestParam String code, @RequestParam String state) throws IOException {
        OAuth2AccessToken oAuth2AccessToken;
        oAuth2AccessToken = naverLoginBO.getAccessToken(code, state);
        OauthResponseDto responseDto = naverLoginBO.getUserProfile(oAuth2AccessToken);
        SignupResponseDto signupResponseDto = new SignupResponseDto(responseDto, LoginProvider.NAVER);

        Optional<Member> memberOptional = memberService.findMemberBySnsId(responseDto.getSnsId()); // DB에 Member가 존재하지 않으면 추가회원가입 진행함
        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(signupResponseDto);
        }
        String accessToken = tokenProvider.createAccessToken(signupResponseDto.getSnsId());
        String refreshToken = tokenProvider.createRefreshToken(signupResponseDto.getSnsId());
        authService.saveRefreshToken(refreshToken, memberOptional.get().getSnsId());

        return ResponseEntity.ok()
                .header("accessToken", accessToken)
                .header("refreshToken", refreshToken)
                .body(signupResponseDto);
    }

    @RequestMapping(value = "/kakao/callback", method = {RequestMethod.GET, RequestMethod.POST}) // 카카오 로그인
    public ResponseEntity<SignupResponseDto> kakaoLogin(@RequestParam String code) throws IOException {
        OAuth2AccessToken oAuth2AccessToken;
        oAuth2AccessToken = kakaoLoginBO.getAccessToken(code);
        OauthResponseDto responseDto = kakaoLoginBO.getUserProfile(oAuth2AccessToken);
        SignupResponseDto signupResponseDto = new SignupResponseDto(responseDto, LoginProvider.KAKAO);

        Optional<Member> memberOptional = memberService.findMemberBySnsId(responseDto.getSnsId()); // DB에 Member가 존재하지 않으면 추가회원가입 진행함
        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(signupResponseDto);
        }
        String accessToken = tokenProvider.createAccessToken(signupResponseDto.getSnsId());
        String refreshToken = tokenProvider.createRefreshToken(signupResponseDto.getSnsId());
        authService.saveRefreshToken(refreshToken, memberOptional.get().getSnsId());

        return ResponseEntity.ok()
                .header("accessToken", accessToken)
                .header("refreshToken", refreshToken)
                .body(signupResponseDto);
    }

    @PostMapping("/member/signup") // 추가 회원가입
    ResponseEntity<String> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        memberService.signup(signupRequestDto);
        return ResponseEntity.ok()
                .body("추가 회원가입 성공");
    }
}
