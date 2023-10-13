package codingFriends_Server.domain.SummaryCode.Dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SummaryCodeTitleContentResponseDto {
	private String content;
	private String title;
}
