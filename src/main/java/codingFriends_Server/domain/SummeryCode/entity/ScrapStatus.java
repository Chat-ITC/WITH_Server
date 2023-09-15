package codingFriends_Server.domain.SummeryCode.entity;

public enum ScrapStatus {
    Yes("Yes"), No("No");

    private String value;

    ScrapStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
