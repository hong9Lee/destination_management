package travel.util.helper.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import travel.util.helper.enums.StatusCode;

import java.time.format.DateTimeParseException;


@ControllerAdvice
@Slf4j
public class GlobalControllerAdvice {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity exception(Exception e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR.getCode());
        errorResponse.setMessage(e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }


    /**
     * @valid Request 유효성 체크에 통과하지 못하면 MethodArgumentNotValidException 발생.
     */
    @ExceptionHandler({MethodArgumentNotValidException.class,
            MissingRequestHeaderException.class,
            ExistCityException.class})
    public ResponseEntity BadRequestException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusCode(StatusCode.BAD_REQUEST.getCode());
        errorResponse.setMessage(e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @ExceptionHandler({
            EmptyResultDataAccessException.class,
            DateTimeParseException.class
    })
    public ResponseEntity InternalServerException(Exception e) {
        log.error(e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR.getCode());
        errorResponse.setMessage(e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }


}
