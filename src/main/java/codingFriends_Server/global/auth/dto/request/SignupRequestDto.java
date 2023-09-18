package codingFriends_Server.global.auth.dto.request;

import codingFriends_Server.domain.Member.entity.Member;
import codingFriends_Server.global.auth.oauth.LoginProvider;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class SignupRequestDto { // 추가 회원가입을 할 때 받는 정보
    private String name;
    private String email;
    private LoginProvider loginProvider;
    private String snsId;
    @NotBlank
    private String user_level;
    @NotBlank
    private String skill_language;

    public Member of() {
        return Member.builder()
                .snsId(snsId)
                .name(name)
                .email(email)
                .loginProvider(loginProvider)
                .user_level(user_level)
                .skill_language(skill_language)
                .build();
    }
}
