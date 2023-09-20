package codingFriends_Server.domain.SummaryCode.Dto.response;

import codingFriends_Server.domain.SummaryCode.entity.ScrapSummaryCode;
import codingFriends_Server.domain.SummaryCode.entity.SummaryCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SummaryCodeResponseDto {
    private Long id;
    private String title;
    private String content;
    private String fav_language;
    private LocalDateTime createAt;
    private String isScrapped;

    public SummaryCodeResponseDto(SummaryCode summaryCode) {
        this.id = summaryCode.getId();
        this.title = summaryCode.getTitle();
        this.content = summaryCode.getContent();
        this.fav_language = summaryCode.getFav_language();
        this.createAt = summaryCode.getCreatedAt();
        this.isScrapped = summaryCode.getIsScrapped();
    }

    public SummaryCodeResponseDto(ScrapSummaryCode scrapSummaryCode) {
        this.id = scrapSummaryCode.getId();
        this.title = scrapSummaryCode.getTitle();
        this.content = scrapSummaryCode.getContent();
        this.createAt = scrapSummaryCode.getCreatedAt();
        this.fav_language = scrapSummaryCode.getFav_language();
    }
}
