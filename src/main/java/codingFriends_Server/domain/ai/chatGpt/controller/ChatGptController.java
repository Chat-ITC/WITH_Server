package codingFriends_Server.domain.ai.chatGpt.controller;

import codingFriends_Server.domain.ai.chatGpt.response.ChatResponse;
import codingFriends_Server.domain.ai.chatGpt.service.ChatGptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/chat-gpt")
public class ChatGptController {

    private final ChatGptService communityService;

    @PostMapping("")
    public ResponseEntity<String> ChatGpt(@RequestBody String prompt) {

        String chatResponse = communityService.askQuestion(prompt);
        System.out.println("chatResponse = " + chatResponse);
        return ResponseEntity.ok(chatResponse);

    }
//git merge test
}