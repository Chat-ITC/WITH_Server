package codingFriends_Server.domain.Member.dto.response;

import codingFriends_Server.domain.Member.entity.Member;
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
    private String fav_language;
    private String skill_language;

    public MemberInfoResponseDto(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
        this.fav_language = member.getFav_language();
        this.skill_language = member.getSkill_language();
    }
}
