package codingFriends_Server.domain.SummaryCode.Dto.response;

import codingFriends_Server.domain.SummaryCode.entity.SummaryCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SummaryCodeResponseDto {
    private String title;
    private String content;
    private String fav_language;
    private LocalDateTime createAt;

    public SummaryCodeResponseDto(SummaryCode summaryCode) {
        this.title = summaryCode.getTitle();
        this.content = summaryCode.getContent();
        this.fav_language = summaryCode.getFav_language();
        this.createAt = summaryCode.getCreatedAt();
    }
}
