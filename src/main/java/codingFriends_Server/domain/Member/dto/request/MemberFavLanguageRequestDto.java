package codingFriends_Server.domain.Member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberFavLanguageRequestDto { // 유저가 좋아하는 언어
	private String skill_language;
}
