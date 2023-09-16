package codingFriends_Server.domain.ai.chatGpt.service;

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

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatGptService {
    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.api-key}")
    private String openaiApiKey;

    private HttpEntity<ChatRequest> getHttpEntity(ChatRequest chatRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + openaiApiKey);
        HttpEntity<ChatRequest> httpRequest = new HttpEntity<>(chatRequest, headers);
        return httpRequest;
    }

    @Async
    public ChatResponse askQuestion(String prompt) {
        ChatRequest chatRequest = new ChatRequest(model, prompt);
        RestTemplate restTemplate = new RestTemplate();
        ChatResponse response = restTemplate.postForObject(apiUrl, getHttpEntity(chatRequest), ChatResponse.class);
        return response;
    }
}


