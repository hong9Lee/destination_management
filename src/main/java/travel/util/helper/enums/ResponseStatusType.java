package travel.util.helper.enums;

public enum ResponseStatusType {
    OK("200", "SUCCESS"),
    BAD_REQUEST("400", "BAD REQUEST"),
    NOT_FOUND("404", "NOT FOUND"),
    INTERNAL_SERVER_ERROR("500", "INTERNAL SERVER ERROR");

    String code;
    String message;

    ResponseStatusType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
