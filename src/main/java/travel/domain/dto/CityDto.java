package travel.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CityDto {

    private long cityId;
    private String type;
    private String cityName;
    private String explanation;
    private String cityToken;

}
