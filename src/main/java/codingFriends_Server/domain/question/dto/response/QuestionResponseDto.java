package codingFriends_Server.domain.question.dto.response;

import codingFriends_Server.domain.question.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponseDto {
    private String content;
    private String answer;

    public QuestionResponseDto(Question question) {
        this.content = question.getContent();
        this.answer = question.getAnswer();
    }
}
