package travel.util.helper.enums;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public enum StatusCode {
    OK(200, "SUCCESS"),
    BAD_REQUEST(400, "BAD_REQUEST"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR");


    private Integer code;
    private String msg;

    StatusCode(StatusCode status) {
        this.code = status.getCode();
        this.msg = status.getMsg();
    }

    StatusCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
