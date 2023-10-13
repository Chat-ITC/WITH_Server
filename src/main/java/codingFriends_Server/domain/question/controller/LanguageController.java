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

	@PostMapping("/language/save") // DB에 언어를 저장
	public ResponseEntity<String> saveLanguage(@RequestParam("type") String type) {
		String result = languageService.save(type);
		return ResponseEntity.ok()
			.body(result);
	}

	@PatchMapping("/language/update/{id}") // DB에 저장된 언어를 수정
	public ResponseEntity<String> updateLanguage(
		@PathVariable Long id,
		@RequestParam("type") String type) {
		languageService.updateLanguage(id, type);
		return ResponseEntity.ok()
			.body("Language 수정 완료");
	}

	@DeleteMapping("/language/delete/{id}") // DB에 저장된 언어를 삭제
	public ResponseEntity<String> deleteLanguage(
		@PathVariable Long id) {

		languageService.deleteLanguage(id);
		return ResponseEntity.ok()
			.body("Language 삭제 완료");
	}
}
