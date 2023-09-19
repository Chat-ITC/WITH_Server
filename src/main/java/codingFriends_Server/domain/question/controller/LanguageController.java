package codingFriends_Server.domain.question.controller;

import codingFriends_Server.domain.question.service.LanguageService;
import codingFriends_Server.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LanguageController {
    private final LanguageService languageService;

    @PostMapping("/language/save")
    public ResponseEntity<?> saveLanguage(@RequestParam("type") String type) {
        if (type == null) {
            throw new CustomException(HttpStatus.NOT_FOUND, "type 문자열이 없습니다.");
        }
        String result = languageService.save(type);
        return ResponseEntity.ok()
                .body(result);
    }

    @PatchMapping("/language/update")
    public ResponseEntity<?> updateLanguage(
            @RequestParam("id") Long id,
            @RequestParam("type") String type) {
        if (id == null || type == null) {
            throw new CustomException(HttpStatus.NOT_FOUND, "id 또는 문자열이 없습니다.");
        }
        languageService.updateLanguage(id, type);
        return ResponseEntity.ok()
                .body("Language 수정 완료");
    }
    @DeleteMapping("/language/delete")
    public ResponseEntity<?> deleteLanguage(
            @RequestParam("id") Long id) {
        if (id == null) {
            throw new CustomException(HttpStatus.NOT_FOUND, "id가 없습니다.");
        }
        languageService.deleteLanguage(id);
        return ResponseEntity.ok()
                .body("Language 삭제 완료");
    }
}
