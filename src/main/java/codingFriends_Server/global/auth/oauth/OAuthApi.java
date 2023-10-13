package codingFriends_Server.global.auth.oauth;

import com.github.scribejava.core.builder.api.DefaultApi20;

public class OAuthApi {

	public static class NaverOAuthApi extends DefaultApi20 {

		protected NaverOAuthApi() {
		}

		private static class InstanceHolder {
			private static final NaverOAuthApi INSTANCE = new NaverOAuthApi();
		}

		public static NaverOAuthApi instance() {
			return InstanceHolder.INSTANCE;
		}

		@Override
		public String getAccessTokenEndpoint() {
			return "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code";

		}

		@Override
		protected String getAuthorizationBaseUrl() {
			return "https://nid.naver.com/oauth2.0/authorize";
		}

	}

	public static class KakaoOAuthApi extends DefaultApi20 {

		protected KakaoOAuthApi() {
		}

		private static class InstanceHolder {
			private static final KakaoOAuthApi INSTANCE = new KakaoOAuthApi();
		}

		public static KakaoOAuthApi instance() {
			return InstanceHolder.INSTANCE;
		}

		@Override
		public String getAccessTokenEndpoint() {
			return "https://kauth.kakao.com/oauth/token";
		}

		@Override
		protected String getAuthorizationBaseUrl() {
			return "https://kauth.kakao.com/oauth/authorize";
		}
	}

}
