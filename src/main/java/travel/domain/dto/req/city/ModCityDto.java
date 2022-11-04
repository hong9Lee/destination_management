package travel.domain.dto.req.city;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class ModCityDto {

    @NotNull(message = "city id를 정확하게 입력해주세요.")
    private Long cityId;

    private String explanation;

    @NotNull(message = "주소를 정확하게 입력해주세요.")
    private String addr_1; // 도
    @NotNull(message = "주소를 정확하게 입력해주세요.")
    private String addr_2; // 시
    @NotNull(message = "주소를 정확하게 입력해주세요.")
    private String cityName;

    private String fullAddr;

    public String getFullAddr() {
        return this.getAddr_1() + " " + this.getAddr_2() + " " + this.cityName;
    }
}
