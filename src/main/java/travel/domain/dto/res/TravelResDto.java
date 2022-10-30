package travel.domain.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class TravelResDto {

    private Long travelId;
    private Integer code;
    private String msg;


}
