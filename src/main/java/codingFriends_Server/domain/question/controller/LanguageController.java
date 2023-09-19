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
        String result = languageService.save(type);
        return ResponseEntity.ok()
                .body(result);
    }

    @PatchMapping("/language/update/{id}")
    public ResponseEntity<?> updateLanguage(
            @PathVariable Long id,
            @RequestParam("type") String type) {
        languageService.updateLanguage(id, type);
        return ResponseEntity.ok()
                .body("Language 수정 완료");
    }
    @DeleteMapping("/language/delete/{id}")
    public ResponseEntity<?> deleteLanguage(
            @PathVariable Long id) {

        languageService.deleteLanguage(id);
        return ResponseEntity.ok()
                .body("Language 삭제 완료");
    }
}
