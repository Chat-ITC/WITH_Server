package codingFriends_Server.domain.question.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionRequestDto {

	private String title;
	private String content;
	private String answer;
	private String level;
}
