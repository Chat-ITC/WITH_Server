package codingFriends_Server.domain.question.dto.response;

import codingFriends_Server.domain.question.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class QuestionTitleResponseDto {
    private Long id;
    private String title;

    public QuestionTitleResponseDto(Question question) {
        this.id = question.getId();
        this.title = question.getTitle();
    }
}
