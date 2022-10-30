package travel.domain.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class TravelDto {

    private Long travelId;

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long cityId;

    private Long userId;


}
