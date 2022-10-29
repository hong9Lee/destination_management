package travel.util.helper.exception;

import lombok.Data;

import java.util.List;

@Data
public class ErrorResponse {
    String status;
    String message;
}
