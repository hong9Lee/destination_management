package travel.domain.dto.req.city;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class DelCityDto {

    @NotNull
    private Long cityId;
}
