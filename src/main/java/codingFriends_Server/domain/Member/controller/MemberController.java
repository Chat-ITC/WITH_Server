package codingFriends_Server.domain.Member.controller;

import codingFriends_Server.domain.Member.dto.request.MemberLanguageUpdateRequestDto;
import codingFriends_Server.domain.Member.dto.request.MemberSkillUpdateRequestDto;
import codingFriends_Server.domain.Member.dto.response.MemberInfoResponseDto;
import codingFriends_Server.domain.Member.entity.Member;
import codingFriends_Server.domain.Member.service.MemberService;
import codingFriends_Server.global.auth.jwt.MemberPrincipal;
import codingFriends_Server.global.auth.service.AuthService;
import codingFriends_Server.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;

    @GetMapping("/hello")
    public String helloController() {
        return "hello";
    }


    @PatchMapping("/member/update/skill") // member skill 수정
    public ResponseEntity<?> updateMemberSkill(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @RequestBody MemberSkillUpdateRequestDto memberSkillUpdateRequestDto) {
        String snsId = memberPrincipal.getMember().getSnsId();
        MemberInfoResponseDto updateMember = memberService.updateMemberSkill(snsId, memberSkillUpdateRequestDto);
        return ResponseEntity.ok()
                .body(updateMember);
    }

    @PatchMapping("/member/update/language") // member language 수정
    public ResponseEntity<?> updateMemberLanguage(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @RequestBody MemberLanguageUpdateRequestDto memberLanguageUpdateRequestDto) {
        String snsId = memberPrincipal.getMember().getSnsId();
        MemberInfoResponseDto updateMember = memberService.updateMemberLanguage(snsId, memberLanguageUpdateRequestDto);
        return ResponseEntity.ok()
                .body(updateMember);
    }

    @GetMapping("/member/update") //member 정보 조회
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
