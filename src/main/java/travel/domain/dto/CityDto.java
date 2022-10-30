package travel.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CityDto {

    private long cityId;

    private String addr_1; // 도
    private String addr_2; // 시
    private String cityName;

    private String fullAddr;
    private String explanation;

}
