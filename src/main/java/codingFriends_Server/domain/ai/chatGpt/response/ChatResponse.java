package codingFriends_Server.domain.ai.chatGpt.response;

import codingFriends_Server.domain.ai.chatGpt.dto.Message;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {

    private List<Choice> choices;
    private Object Usage;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Choice {

        private int index;
        private Message message;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Usage {

        @JsonProperty("prompt_tokens")
        private int promptTokens;
        @JsonProperty("completion_tokens")
        private int completionTokens;
        @JsonProperty("total_tokens")
        private int totalTokens;
    }
}