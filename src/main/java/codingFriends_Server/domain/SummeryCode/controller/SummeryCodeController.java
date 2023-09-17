package codingFriends_Server.domain.SummeryCode.controller;

import codingFriends_Server.domain.SummeryCode.service.SummeryService;
import codingFriends_Server.global.auth.jwt.MemberPrincipal;
import codingFriends_Server.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SummeryCodeController {
    private final SummeryService summeryService;
    @PostMapping("/ai/summery/like")
    public ResponseEntity<?> saveSummeryCode(@RequestBody String chat_result, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        if (chat_result == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "글이 없습니다.");
        }
        summeryService.save_likeSummeryCode(chat_result, memberPrincipal.getMember());
        return ResponseEntity.ok()
                .body("좋아요를 누른 글을 성공적으로 저장했습니다.");
    }
}
