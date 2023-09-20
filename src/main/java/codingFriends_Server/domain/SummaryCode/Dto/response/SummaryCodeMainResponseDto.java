package codingFriends_Server.domain.SummaryCode.Dto.response;

import codingFriends_Server.domain.SummaryCode.entity.SummaryCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SummaryCodeMainResponseDto {
    private Long id;
    private String title;
    private String content;

    public SummaryCodeMainResponseDto(SummaryCode summaryCode) {
        this.id = summaryCode.getId();
        this.title = summaryCode.getTitle();
        this.content = summaryCode.getContent();
    }
}
