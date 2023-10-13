package codingFriends_Server.domain.ai.chatGpt.dto.response;

import codingFriends_Server.domain.ai.chatGpt.dto.Message;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatGptResponseDto {

	private List<ChatGptChoiceResponseDto> choices;
	private Object Usage;
}