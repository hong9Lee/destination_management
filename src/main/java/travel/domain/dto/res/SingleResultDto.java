package travel.domain.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingleResultDto<T> {

    private Integer code;
    private String msg;
    private T result;


}
