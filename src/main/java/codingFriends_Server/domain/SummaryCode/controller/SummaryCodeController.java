package codingFriends_Server.domain.SummaryCode.controller;

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
    public ResponseEntity<SummaryCodeTitleContentResponseDto> summaryCode(
            @RequestParam("imageFile") MultipartFile multipartFile,
            @RequestParam("question")String question,
            @RequestParam("fav_language")String fav_language,
            @AuthenticationPrincipal MemberPrincipal memberPrincipal
    ) {
        try {
            log.info(multipartFile.toString());
            log.info("멀티 파트 파일");
            log.info(question);
            log.info(fav_language);
            log.info(memberPrincipal.getMember().getSnsId());

            File file = File.createTempFile("temp", null);
            multipartFile.transferTo(file);
            String ocr_result = ocrGeneralService.processImage(apiURL, secretKey, file.getPath());
            log.info("OCR 결과");
            log.info(ocr_result);
            if (ocr_result == null) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "response가 비어있습니다.");
            }
            SummaryCodeTitleContentResponseDto responseDto =
                    chatGptService.askQuestion(ocr_result,question, fav_language,memberPrincipal.getMember());
            log.info("60번째 줄");
            log.info(responseDto.toString());
            summaryService.saveSummaryCode(responseDto, memberPrincipal.getMember());
            return ResponseEntity.ok()
                    .body(responseDto);

        } catch (Exception e) {
            log.info("에러 발생");
            log.info(e.getStackTrace().toString());
            log.info("로그 스택");
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getStackTrace().toString());
        }
    }

    @PostMapping("/ai/summary/like")
    public ResponseEntity<String> saveSummaryCode(@RequestBody SummaryCodeTitleContentResponseDto chat_result, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        if (chat_result == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "글이 없습니다.");
        }
        summaryService.save_likeSummaryCode(chat_result, memberPrincipal.getMember());
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
