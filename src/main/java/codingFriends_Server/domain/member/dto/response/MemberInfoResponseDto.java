package codingFriends_Server.domain.member.dto.response;

import codingFriends_Server.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberInfoResponseDto {
	private String name;
	private String email;
	private String user_level;
	private String skill_language;

	public MemberInfoResponseDto(Member member) {
		this.name = member.getName();
		this.email = member.getEmail();
		this.user_level = member.getUser_level();
		this.skill_language = member.getSkill_language();
	}
}
