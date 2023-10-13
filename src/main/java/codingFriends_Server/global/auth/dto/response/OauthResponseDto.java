package codingFriends_Server.global.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OauthResponseDto { // Oauth로부터 얻는 사용자 정보
	private String snsId;
	private String name;
	private String email;
}
