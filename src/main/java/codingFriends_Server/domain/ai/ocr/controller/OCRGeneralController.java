package codingFriends_Server.domain.ai.ocr.controller;

import codingFriends_Server.domain.ai.ocr.service.OCRGeneralService;
import codingFriends_Server.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequiredArgsConstructor
public class OCRGeneralController {

    private final OCRGeneralService ocrService;

    @Value("${ocr.url}")
    String apiURL;
    @Value("${ocr.key}")
    String secretKey;

    @PostMapping("/processImage")
    public String processImage(@RequestParam("imageFile") MultipartFile imageFile) {
        try {
            File file = File.createTempFile("temp", null);
            imageFile.transferTo(file);
            String result = ocrService.processImage(apiURL, secretKey, file.getPath());
            if (result.isEmpty()) {
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "response가 비어있습니다.");
            }
            return ocrService.processImage(apiURL, secretKey, file.getPath());
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
