package travel.domain.dto.req.travel;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class DelTravelDto {

    @NotNull(message = "travel id는 필수값입니다.")
    private Long travelId;

}
