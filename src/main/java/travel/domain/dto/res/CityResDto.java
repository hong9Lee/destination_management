package travel.domain.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CityResDto {

    private Long cityId;
    private Integer code;
    private String msg;


}
