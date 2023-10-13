package codingFriends_Server.global.auth.oauth.naver;

import codingFriends_Server.global.auth.dto.response.OauthResponseDto;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static codingFriends_Server.global.auth.oauth.OAuthApi.*;

@Component
public class NaverLoginBO {
	@Value("${spring.client.id}")
	private String CLIENT_ID;

	@Value("${spring.client.secret}")
	private String CLIENT_SECRET;

	@Value("${spring.client.redirect-uri}")
	private String REDIRECT_URI;

	@Value("${spring.client.session-state}")
	private String SESSION_STATE;

	@Value("${spring.client.profile-api-url}")
	private String PROFILE_API_URL;

	//    네이버아이디로 Callback처리 및 AccessToken 획득 Method
	public OAuth2AccessToken getAccessToken(String code, String state) throws IOException {
		//Callback으로 전달받은 세션검증용 난수값과 저장되어있는 값이 일치하는지 확인
		OAuth20Service oAuth20Service = new ServiceBuilder()
			.apiKey(CLIENT_ID)
			.apiSecret(CLIENT_SECRET)
			.callback(REDIRECT_URI)
			.state(state)
			.build(NaverOAuthApi.instance());
		OAuth2AccessToken accessToken = oAuth20Service.getAccessToken(code);
		return accessToken;
	}

	//    AccessToken을 이용해 네이버 사용자프로필 API를 호출
	public OauthResponseDto getUserProfile(OAuth2AccessToken oAuth2AccessToken) throws IOException {
		OAuth20Service oAuth20Service = new ServiceBuilder()
			.apiKey(CLIENT_ID)
			.apiSecret(CLIENT_SECRET)
			.callback(REDIRECT_URI).build(NaverOAuthApi.instance());
		OAuthRequest request = new OAuthRequest(Verb.GET, PROFILE_API_URL, oAuth20Service);
		oAuth20Service.signRequest(oAuth2AccessToken, request);
		Response response = request.send();

		JSONObject json = new JSONObject(response.getBody());
		String name = json.getJSONObject("response").getString("name");
		String email = json.getJSONObject("response").getString("email");
		String id = json.getJSONObject("response").getString("id");

		OauthResponseDto socialUserInfoDto = new OauthResponseDto(id, name, email);
		return socialUserInfoDto;
	}
}