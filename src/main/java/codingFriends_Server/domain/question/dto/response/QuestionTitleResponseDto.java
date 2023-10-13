package codingFriends_Server.domain.question.dto.response;

import codingFriends_Server.domain.question.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class QuestionTitleResponseDto {
	private String title;
	private String content;
	private String answer;

	public QuestionTitleResponseDto(Question question) {
		this.title = question.getTitle();
		this.content = question.getContent();
		this.answer = question.getAnswer();
	}
}
