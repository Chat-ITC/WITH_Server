package codingFriends_Server.domain.SummaryCode.controller;

import codingFriends_Server.domain.SummaryCode.Dto.response.SummaryCodeMainResponseDto;
import codingFriends_Server.domain.SummaryCode.Dto.response.SummaryCodeResponseDto;
import codingFriends_Server.domain.SummaryCode.Dto.response.SummaryCodeTitleContentResponseDto;
import codingFriends_Server.domain.SummaryCode.entity.SummaryCode;
import codingFriends_Server.domain.SummaryCode.service.SummaryService;
import codingFriends_Server.domain.ai.chatGpt.service.ChatGptService;
import codingFriends_Server.domain.ai.ocr.service.OCRGeneralService;
import codingFriends_Server.global.auth.jwt.MemberPrincipal;
import codingFriends_Server.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SummaryCodeController {
    private final OCRGeneralService ocrGeneralService;
    private final ChatGptService chatGptService;
    private final SummaryService summaryService;
    @Value("${spring.ocr.url}")
    String apiURL;
    @Value("${spring.ocr.key}")
    String secretKey;


    @PostMapping("/ai/summary")
    public ResponseEntity<SummaryCodeMainResponseDto> summaryCode(
            @RequestParam("imageFile") MultipartFile multipartFile,
            @RequestParam("question")String question,
            @RequestParam("fav_language")String fav_language,
            @AuthenticationPrincipal MemberPrincipal memberPrincipal
    ) {
        try {
            File file = File.createTempFile("temp", null);
            multipartFile.transferTo(file);
            String ocr_result = ocrGeneralService.processImage(apiURL, secretKey, file.getPath());

            if (ocr_result == null) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "response가 비어있습니다.");
            }
            SummaryCodeTitleContentResponseDto responseDto =
                    chatGptService.askQuestion(ocr_result,question, fav_language, memberPrincipal.getMember());

            SummaryCodeMainResponseDto summaryCodeMainResponseDto =
                    summaryService.saveSummaryCode(responseDto, fav_language, memberPrincipal.getMember());

            return ResponseEntity.ok()
                    .body(summaryCodeMainResponseDto);

        } catch (Exception e) {
            log.error("에러 발생: " + e.getMessage()); // 에러 메시지 출력
            log.error("스택 트레이스: ", e); // 스택 트레이스 출력
            throw new CustomException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }

    @PostMapping("/ai/summary/like")
    public ResponseEntity<String> saveSummaryCode(@RequestParam Long id) {

        summaryService.save_likeSummaryCode(id);
        return ResponseEntity.ok()
                .body("좋아요를 누른 글을 성공적으로 저장했습니다.");
    }
    @GetMapping("/ai/summary/home")
    public ResponseEntity<?> getSummaryContents(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        List<SummaryCodeResponseDto> summaryCodeResponseDtoList = summaryService.getSummaryCodeByMember(memberPrincipal.getMember());
        if (summaryCodeResponseDtoList.isEmpty()) {
            return ResponseEntity.ok()
                    .body("요약한 글이 없습니다.");
        }
        return ResponseEntity.ok()
                .body(summaryCodeResponseDtoList);
    }

    @GetMapping("/ai/summary/home/scrap")
    public ResponseEntity<?> getScrapContents(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        List<SummaryCodeResponseDto> summaryCodeResponseDtoList = summaryService.getScrapSummaryContents(memberPrincipal.getMember());

        if (summaryCodeResponseDtoList.isEmpty()) {
            return ResponseEntity.ok()
                    .body("요약한 글이 없습니다.");
        }
        return ResponseEntity.ok()
                .body(summaryCodeResponseDtoList);
    }

}
