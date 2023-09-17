package codingFriends_Server.domain.Member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Struct;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateRequestDto {
    private String fav_language;
    private String skill_language;
}
