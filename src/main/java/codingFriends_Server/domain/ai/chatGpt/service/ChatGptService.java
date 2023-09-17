package codingFriends_Server.domain.ai.chatGpt.service;

import codingFriends_Server.domain.ai.chatGpt.dto.Message;
import codingFriends_Server.domain.ai.chatGpt.request.ChatRequest;
import codingFriends_Server.domain.ai.chatGpt.response.ChatResponse;
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

    private HttpEntity<ChatRequest> getHttpEntity(ChatRequest chatRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + openaiApiKey);
        HttpEntity<ChatRequest> httpRequest = new HttpEntity<>(chatRequest, headers);
        return httpRequest;
    }

    @Async
    public String askQuestion(String prompt) {
        ChatRequest chatRequest = new ChatRequest(model, prompt);
        RestTemplate restTemplate = new RestTemplate();
        ChatResponse response = restTemplate.postForObject(apiUrl, getHttpEntity(chatRequest), ChatResponse.class);

        List<ChatResponse.Choice> choiceList = response.getChoices();

        List<String> messageTexts = choiceList.stream().map(ChatResponse.Choice::getMessage)
                .map(Message::getContent)
                .collect(Collectors.toList());
        System.out.println("response = " + response);
        return String.join("\n", messageTexts);
    }
}

