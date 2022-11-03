package travel.domain.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityResultDto {

    private long id;
    private String isTraveling;
    private String addr_1; // 도
    private String addr_2; // 시
    private String cityName;
    private String fullAddr;
    private String explanation;

}
