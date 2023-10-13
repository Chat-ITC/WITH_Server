package codingFriends_Server.global.auth.oauth;

public enum LoginProvider {
	KAKAO("KAKAO"), NAVER("NAVER");

	private String value;

	LoginProvider(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
