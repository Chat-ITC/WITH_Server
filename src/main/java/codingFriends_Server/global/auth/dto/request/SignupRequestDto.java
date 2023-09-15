package codingFriends_Server.global.auth.dto.request;

import codingFriends_Server.domain.Member.entity.Member;
import codingFriends_Server.global.auth.oauth.LoginProvider;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class SignupRequestDto {
    private String name;
    private String email;
    private LoginProvider loginProvider;
    private String snsId;
    @NotBlank
    private String fav_language;
    @NotBlank
    private String skill_language;

    public Member of() {
        return Member.builder()
                .snsId(snsId)
                .name(name)
                .email(email)
                .loginProvider(loginProvider)
                .fav_language(fav_language)
                .skill_language(skill_language)
                .build();
    }
}
