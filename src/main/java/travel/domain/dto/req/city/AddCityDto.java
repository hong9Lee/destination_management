package travel.domain.dto.req.city;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class AddCityDto {
    private String explanation;

    @NotNull
    private String addr_1; // 도
    @NotNull
    private String addr_2; // 시
    @NotNull
    private String cityName;

    private String fullAddr;

    public String getFullAddr() {
        return this.getAddr_1() + " " + this.getAddr_2() + " " + this.cityName;
    }
}
