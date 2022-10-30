package travel.domain.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import travel.domain.dto.TravelDto;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TravelSingleResultDto {

    private Integer code;
    private String msg;
    private TravelDto result;


}
