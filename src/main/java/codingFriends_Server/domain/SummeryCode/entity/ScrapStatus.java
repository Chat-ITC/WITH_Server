package codingFriends_Server.domain.SummeryCode.entity;

public enum ScrapStatus { // 스크랩 여부를 구분하는 enum 클래스
    Yes("Yes"), No("No");

    private String value;

    ScrapStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
