package codingFriends_Server.global.auth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponseDto {
    private String AccessToken;
    private String RefreshToken;
}
