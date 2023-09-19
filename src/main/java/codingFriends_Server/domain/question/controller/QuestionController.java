package codingFriends_Server.domain.question.controller;

import codingFriends_Server.domain.question.dto.request.QuestionRequestDto;
import codingFriends_Server.domain.question.dto.request.QuestionUpdateRequestDto;
import codingFriends_Server.domain.question.dto.response.QuestionResponseDto;
import codingFriends_Server.domain.question.dto.response.QuestionTitleResponseDto;
import codingFriends_Server.domain.question.service.QuestionService;
import codingFriends_Server.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping("/question/save")
    public ResponseEntity<QuestionRequestDto> saveQuestion(
            @RequestParam("type") String type,
            @RequestBody QuestionRequestDto questionRequestDto) {

        questionService.saveQuestion(questionRequestDto, type);
        return ResponseEntity.ok()
                .body(questionRequestDto);
    }

    @GetMapping("/question/get/language")
    public ResponseEntity<?> getQuestionTitleFromLanguage(
            @RequestParam("type") String type) {
        List<QuestionTitleResponseDto> questionList = questionService.findQuestionByLanguage(type);

        if (questionList.isEmpty()) {
            return ResponseEntity.ok()
                    .body("question이 비어있습니다.");
        }
        return ResponseEntity.ok()
                .body(questionList);
    }

    @GetMapping("/question/get/{id}")
    public ResponseEntity<?> getQuestionFromTitle(
            @PathVariable Long id) {
        QuestionResponseDto questionResponseDto = questionService.getQuestionFromTitle(id);
        return ResponseEntity.ok()
                .body(questionResponseDto);
    }

    @PatchMapping("/question/update/{id}")
    public ResponseEntity<?> updateQuestion(
            @PathVariable Long id,
            @RequestBody QuestionUpdateRequestDto questionUpdateRequestDto) {
        questionService.updateQuestion(id, questionUpdateRequestDto);
        return ResponseEntity.ok()
                .body("question 수정 완료");
    }

    @DeleteMapping("/question/delete/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.ok()
                .body("question 삭제 완료");
    }

}
