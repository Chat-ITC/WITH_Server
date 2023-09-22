package codingFriends_Server.domain.Member.controller;

import codingFriends_Server.domain.Member.dto.request.MemberFavLanguageRequestDto;
import codingFriends_Server.domain.Member.dto.request.MemberlevelUpdateRequestDto;
import codingFriends_Server.domain.Member.dto.response.MemberInfoResponseDto;
import codingFriends_Server.domain.Member.service.MemberService;
import codingFriends_Server.global.auth.jwt.MemberPrincipal;
import codingFriends_Server.global.auth.service.AuthService;
import codingFriends_Server.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;


    @PatchMapping("/member/update/level") // member skill 수정
    public ResponseEntity<MemberInfoResponseDto> updateMemberSkill(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @RequestBody MemberlevelUpdateRequestDto memberlevelUpdateRequestDto) {
        String snsId = memberPrincipal.getMember().getSnsId();
        MemberInfoResponseDto updateMember = memberService.updateMemberLevel(snsId, memberlevelUpdateRequestDto);
        return ResponseEntity.ok()
                .body(updateMember);
    }

    @PatchMapping("/member/update/language") // member language 수정
    public ResponseEntity<MemberInfoResponseDto> updateMemberLanguage(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @RequestBody MemberFavLanguageRequestDto memberFavLanguageRequestDto) {
        String snsId = memberPrincipal.getMember().getSnsId();
        MemberInfoResponseDto updateMember = memberService.updateMemberLanguage(snsId, memberFavLanguageRequestDto);
        return ResponseEntity.ok()
                .body(updateMember);
    }

    @GetMapping("/member/update") //member 정보 조회
    public ResponseEntity<MemberInfoResponseDto> getMemberInfo(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        MemberInfoResponseDto memberInfoResponseDto = new MemberInfoResponseDto(memberPrincipal.getMember());
        return ResponseEntity.ok()
                .body(memberInfoResponseDto);
    }

    @PostMapping("/member/logout") //로그아웃
    public ResponseEntity<String> logoutMember(@RequestHeader("refreshToken") String refreshToken,
                                          @RequestHeader("accessToken") String accessToken) {
        if (accessToken == null || refreshToken == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "accessToken이 없습니다.");
        }
        authService.setAccessTokenBlackList(accessToken);
        authService.deleteRefreshToken(refreshToken);
        return ResponseEntity.ok()
                .body("로그아웃 성공");
    }

    @DeleteMapping("/member/delete/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok()
                .body("memberDelete 완료");
    }
}
