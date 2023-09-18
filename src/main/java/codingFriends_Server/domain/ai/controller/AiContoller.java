package codingFriends_Server.domain.ai.controller;

import codingFriends_Server.domain.SummeryCode.service.SummeryService;
import codingFriends_Server.domain.ai.chatGpt.service.ChatGptService;
import codingFriends_Server.domain.ai.ocr.service.OCRGeneralService;
import codingFriends_Server.global.auth.jwt.MemberPrincipal;
import codingFriends_Server.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequiredArgsConstructor
public class AiContoller {
    private final OCRGeneralService ocrGeneralService;
    private final ChatGptService chatGptService;
    private final SummeryService summeryService;

    @Value("${spring.ocr.url}")
    String apiURL;
    @Value("${spring.ocr.key}")
    String secretKey;

    @PostMapping("/ai/summery")
    public ResponseEntity<?> summeryCode(
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

            String chat_result = chatGptService.askQuestion(ocr_result,question,fav_language);
            summeryService.saveSummeryCode(chat_result, memberPrincipal.getMember());
            return ResponseEntity.ok()
                    .body(chat_result);

        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
