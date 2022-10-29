package travel.util.helper.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import travel.util.helper.enums.StatusCode;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@ControllerAdvice
@Slf4j
public class GlobalControllerAdvice {
//    @ExceptionHandler(value = Exception.class)
//    public ResponseEntity exception(Exception e) {
//        System.out.println("GLOBAL+_+");
//        log.error(e.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//    }


    /**
     * @valid Request 유효성 체크에 통과하지 못하면 MethodArgumentNotValidException 발생.
     */
    @ExceptionHandler({MethodArgumentNotValidException.class,
            MissingRequestHeaderException.class})
    public ResponseEntity BadRequestException(MethodArgumentNotValidException e, HttpServletRequest httpServletRequest) {
        log.error(e.getMessage());

//        List<Error> errorList = new ArrayList<>();
//
//        BindingResult bindingResult = e.getBindingResult();
//        bindingResult.getAllErrors().forEach(error -> {
//
//            FieldError field = (FieldError) error;
//            String fieldName = field.getField();
//            String message = field.getDefaultMessage();
//
//            errorList.add(Utils.getErrMsg(fieldName, message));
//        });
//
//        ErrorResponse errResponse = Utils.getErrResponse(errorList,
//                httpServletRequest, ResponseStatusType.BAD_REQUEST);

//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errResponse);
        return null;
    }




    @ExceptionHandler({
            EmptyResultDataAccessException.class,
            EndDateException.class
    })
    public ResponseEntity InternalServerException(Exception e) {
        log.error(e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(StatusCode.FAIL.getCode());
        errorResponse.setMessage(e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);

    }


}
