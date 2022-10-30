package travel.util.helper.exception;

import lombok.Data;

@Data
public class ExistCityException extends RuntimeException{

    public ExistCityException(String message) {
        super(message);
    }
}
