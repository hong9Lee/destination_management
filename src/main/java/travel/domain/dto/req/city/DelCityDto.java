package travel.domain.dto.req.city;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class DelCityDto {

    @NotNull(message = "city id를 입력해주세요.")
    private Long cityId;
}
