package codingFriends_Server.global.auth.jwt;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

	//접근이 거부된 경우(인가 실패) 호출되는 핸들러
	//권한이 없을 때 호출된다.
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException {
		response.sendError(HttpServletResponse.SC_FORBIDDEN);
	}
}
