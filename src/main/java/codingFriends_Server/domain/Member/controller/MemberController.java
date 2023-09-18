package codingFriends_Server.domain.Member.controller;

import codingFriends_Server.domain.Member.dto.request.MemberUpdateRequestDto;
import codingFriends_Server.domain.Member.dto.response.MemberInfoResponseDto;
import codingFriends_Server.domain.Member.entity.Member;
import codingFriends_Server.domain.Member.service.MemberService;
import codingFriends_Server.global.auth.jwt.MemberPrincipal;
import codingFriends_Server.global.auth.jwt.TokenProvider;
import codingFriends_Server.global.auth.service.AuthService;
import codingFriends_Server.global.common.exception.CustomException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;

    @GetMapping("/hello")
    public String helloController() {
        return "hello";
    }


    @PatchMapping("/member/update")
    public ResponseEntity<?> updateMember(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @RequestBody MemberUpdateRequestDto memberUpdateRequestDto) {
        String snsId = memberPrincipal.getMember().getSnsId();
        Member updateMember = memberService.updateMember(snsId, memberUpdateRequestDto);
        return ResponseEntity.ok()
                .body(updateMember);
    }

    @GetMapping("/member/update")
    public ResponseEntity<?> getMemberInfo(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        MemberInfoResponseDto memberInfoResponseDto = new MemberInfoResponseDto(memberPrincipal.getMember());
        return ResponseEntity.ok()
                .body(memberInfoResponseDto);
    }

    @PostMapping("member/logout")
    public ResponseEntity<?> logoutMember(@AuthenticationPrincipal MemberPrincipal memberPrincipal,
                                          @RequestHeader("Authorization") String accessToken) {
        if (accessToken == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "accessToken이 없습니다.");
        }
        authService.setAccessTokenBlackList(accessToken);
        authService.deleteRefreshToken(memberPrincipal.getMember().getSnsId());
        return ResponseEntity.ok()
                .body("로그아웃 성공");
    }
}
