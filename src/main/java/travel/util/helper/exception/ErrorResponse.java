package travel.util.helper.exception;

import lombok.Data;

import java.util.List;

@Data
public class ErrorResponse {
    Integer statusCode;
    String message;
}
