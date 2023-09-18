package codingFriends_Server.global.auth.controller;

import codingFriends_Server.domain.Member.entity.Member;
import codingFriends_Server.domain.Member.service.MemberService;
import codingFriends_Server.global.auth.dto.request.SignupRequestDto;
import codingFriends_Server.global.auth.dto.response.OauthResponseDto;
import codingFriends_Server.global.auth.dto.response.SignupResponseDto;
import codingFriends_Server.global.auth.jwt.MemberPrincipal;
import codingFriends_Server.global.auth.jwt.TokenProvider;
import codingFriends_Server.global.auth.oauth.LoginProvider;
import codingFriends_Server.global.auth.oauth.kakao.KakaoLoginBO;
import codingFriends_Server.global.auth.oauth.naver.NaverLoginBO;
import codingFriends_Server.global.auth.service.AuthService;
import codingFriends_Server.global.common.exception.CustomException;
import com.github.scribejava.core.model.OAuth2AccessToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final NaverLoginBO naverLoginBO;
    private final KakaoLoginBO kakaoLoginBO;
    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final AuthService authService;


     //OAuth는 회원가입과 로그인이 동일해서 추가 회원가입만 고려하면 됨

    @RequestMapping(value = "/naver/callback", method = {RequestMethod.GET, RequestMethod.POST}) // 네이버 로그인
    public ResponseEntity<?> naverLogin(@RequestParam String code, @RequestParam String state) throws IOException {
        OAuth2AccessToken oAuth2AccessToken;
        oAuth2AccessToken = naverLoginBO.getAccessToken(code, state);
        OauthResponseDto responseDto = naverLoginBO.getUserProfile(oAuth2AccessToken);
        SignupResponseDto signupResponseDto = new SignupResponseDto(responseDto, LoginProvider.NAVER);

        Optional<Member> memberOptional = memberService.findMemberBySnsId(responseDto.getSnsId()); // DB에 Member가 존재하지 않으면 추가회원가입 진행함
        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("validation","no")
                    .body(signupResponseDto);
        }
        String accessToken = tokenProvider.createAccessToken(signupResponseDto.getSnsId());
        String refreshToken = tokenProvider.createRefreshToken(signupResponseDto.getSnsId());

        authService.saveRefreshToken(memberOptional.get().getSnsId(),refreshToken);

        return ResponseEntity.ok()
                .header("refreshToken",refreshToken)
                .header("accessToken", accessToken)
                .body(signupResponseDto);
    }

    @RequestMapping(value = "/kakao/callback", method = {RequestMethod.GET, RequestMethod.POST}) // 카카오 로그인
    public ResponseEntity<?> kakaoLogin(@RequestParam String code) throws IOException {
        OAuth2AccessToken oAuth2AccessToken;
        oAuth2AccessToken = kakaoLoginBO.getAccessToken(code);
        OauthResponseDto responseDto = kakaoLoginBO.getUserProfile(oAuth2AccessToken);
        SignupResponseDto signupResponseDto = new SignupResponseDto(responseDto, LoginProvider.KAKAO);

        Optional<Member> memberOptional = memberService.findMemberBySnsId(responseDto.getSnsId()); // DB에 Member가 존재하지 않으면 추가회원가입 진행함
        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("validation","no")
                    .body(signupResponseDto);
        }
        String accessToken = tokenProvider.createAccessToken(signupResponseDto.getSnsId());
        String refreshToken = tokenProvider.createRefreshToken(signupResponseDto.getSnsId());

        authService.saveRefreshToken(memberOptional.get().getSnsId(),refreshToken);

        return ResponseEntity.ok()
                .header("refreshToken",refreshToken)
                .header("accessToken", accessToken)
                .body(signupResponseDto);
    }

    @PostMapping("/member/signup") // 추가 회원가입
    ResponseEntity<String> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        memberService.signup(signupRequestDto);
        return ResponseEntity.ok()
                .body("추가 회원가입 성공");
    }
    @PostMapping("/member/refreshToken") // AccessToken & RefreshToken 재발급
    //jwt 로직에서 제외하기
    public ResponseEntity<?> makeAccessTokenFromRefreshToken(
            @RequestHeader("refreshToken")String refreshToken) {
        if (refreshToken == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "refreshToken이 존재하지 않습니다.");
        }

        String snsId = authService.getsnsIdFromRefreshToken(refreshToken);
        log.info("snsId 값");
        log.info(snsId);
        authService.deleteRefreshToken(refreshToken);
        log.info("삭제 성공");
        String new_accessToken = tokenProvider.createAccessToken(snsId);
        log.info("accessToken 생성 성공");
        String new_refreshToken = tokenProvider.createRefreshToken(snsId);
        log.info("refreshToken 생성 성공");

        authService.saveRefreshToken(refreshToken, snsId);
        log.info("저장 성공");


        return ResponseEntity.ok()
                .header("refreshToken", new_refreshToken)
                .header("accessToken", new_accessToken)
                .body("refreshToken 재발급 완료");
    }
}
