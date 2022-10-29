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

    @NotNull(message = "type은 필수값입니다.")
    private String type; // ADD, MOD, DEL

    private String title;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull(message = "city id는 필수값입니다.")
    private Long cityId;

    @NotNull(message = "user id는 필수값입니다.")
    private Long userId;


}
