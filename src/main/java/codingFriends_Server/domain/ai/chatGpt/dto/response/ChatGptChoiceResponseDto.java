package codingFriends_Server.domain.ai.chatGpt.dto.response;

import codingFriends_Server.domain.ai.chatGpt.dto.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatGptChoiceResponseDto {
    private int index;
    private Message message;
}
