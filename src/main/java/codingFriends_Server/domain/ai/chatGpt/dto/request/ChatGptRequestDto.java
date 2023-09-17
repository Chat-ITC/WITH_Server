package codingFriends_Server.domain.ai.chatGpt.dto.request;

import codingFriends_Server.domain.ai.chatGpt.dto.Message;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatGptRequestDto {

    private String model;
    private List<Message> messages;
    private int max_tokens;

    public ChatGptRequestDto(String model, String content) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("user", content));
        this.max_tokens = 300;
    }

}