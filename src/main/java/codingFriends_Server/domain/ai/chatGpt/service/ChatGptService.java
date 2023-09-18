package codingFriends_Server.domain.ai.chatGpt.service;

import codingFriends_Server.domain.ai.chatGpt.dto.Message;
import codingFriends_Server.domain.ai.chatGpt.dto.request.ChatGptRequestDto;
import codingFriends_Server.domain.ai.chatGpt.dto.response.ChatGptChoiceResponseDto;
import codingFriends_Server.domain.ai.chatGpt.dto.response.ChatGptResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatGptService {
    @Value("${spring.openai.model}")
    private String model;

    @Value("${spring.openai.api.url}")
    private String apiUrl;

    @Value("${spring.openai.api.api-key}")
    private String openaiApiKey;

    private HttpEntity<ChatGptRequestDto> getHttpEntity(ChatGptRequestDto chatRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + openaiApiKey);
        HttpEntity<ChatGptRequestDto> httpRequest = new HttpEntity<>(chatRequest, headers);
        return httpRequest;
    }

    @Async
    public String askQuestion(String ocr_result, String question, String fav_language) {
        String prompt = "분야는 " + fav_language + " 입니다. 만약 '상관없음'이면 코딩 언어에 국한되지 않고 내용에 맞게 대답해줘"
                + "다음 내용을 " + question + " 만약 '선택없음'이라면 내용 요약해줘 " + ocr_result;

        ChatGptRequestDto chatGptRequestDto = new ChatGptRequestDto(model, prompt);
        RestTemplate restTemplate = new RestTemplate();
        ChatGptResponseDto response = restTemplate.postForObject(apiUrl, getHttpEntity(chatGptRequestDto), ChatGptResponseDto.class);

        List<ChatGptChoiceResponseDto> choiceList = response.getChoices();

        List<String> messageTexts = choiceList.stream().map(ChatGptChoiceResponseDto::getMessage)
                .map(Message::getContent)
                .collect(Collectors.toList());
        return String.join("\n", messageTexts);
    }
}

