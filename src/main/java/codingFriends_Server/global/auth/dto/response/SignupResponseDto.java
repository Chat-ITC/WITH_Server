package codingFriends_Server.global.auth.dto.response;

import codingFriends_Server.domain.Member.entity.Member;
import codingFriends_Server.global.auth.oauth.LoginProvider;
import lombok.Data;

@Data
public class SignupResponseDto { //OAuth로부터 얻는 사용자 정보 + 로그인 유형
	private String name;
	private String email;
	private LoginProvider loginProvider;
	private String snsId;

	public Member of() {
		return Member.builder()
			.name(name)
			.email(email)
			.loginProvider(loginProvider)
			.snsId(snsId)
			.build();
	}

	public SignupResponseDto(OauthResponseDto oauthResponseDto, LoginProvider loginProvider) {
		this.name = oauthResponseDto.getName();
		this.email = oauthResponseDto.getEmail();
		this.snsId = oauthResponseDto.getSnsId();
		this.loginProvider = loginProvider;
	}
}
