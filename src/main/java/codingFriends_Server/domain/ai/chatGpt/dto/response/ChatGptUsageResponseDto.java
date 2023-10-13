package codingFriends_Server.domain.ai.chatGpt.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatGptUsageResponseDto {
	@JsonProperty("prompt_tokens")
	private int promptTokens;
	@JsonProperty("completion_tokens")
	private int completionTokens;
	@JsonProperty("total_tokens")
	private int totalTokens;
}

