package travel.domain.dto.req;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class ModTravelDto {

    @NotNull(message = "travel id는 필수값입니다.")
    private Long travelId;

    private String title;

    @NotNull(message = "여행 시작일을 입력해 주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull(message = "city id는 필수값입니다.")
    private Long cityId;

    @NotNull(message = "user id는 필수값입니다.")
    private Long userId;
}
