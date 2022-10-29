package travel.util.helper.exception;

import lombok.Data;

@Data
public class EndDateException extends RuntimeException{

    public EndDateException(String message) {
        super(message);
    }
}
