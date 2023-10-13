package codingFriends_Server.global.auth.jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	//인증되지 않은 사용자가 보호된 리소스에 접근하려고 할 때 호출되는 핸들러입니다.
	//주로 인증되지 않은 사용자의 접근이나 만료된 토큰 등의 상황에서 호출됩니다.
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}
}