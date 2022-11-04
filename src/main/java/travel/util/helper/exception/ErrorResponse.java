package travel.util.helper.exception;

import lombok.Data;

@Data
public class ErrorResponse {
    Integer statusCode;
    String message;
}
