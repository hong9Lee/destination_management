package travel.util.helper.enums;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public enum StatusCode {
    SUCCESS("200", "성공"),
    FAIL("500", "오류");

    private String code;
    private String msg;

    StatusCode(StatusCode status) {
        this.code = status.getCode();
        this.msg = status.getMsg();
    }

    StatusCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
