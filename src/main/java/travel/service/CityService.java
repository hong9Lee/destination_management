package travel.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import travel.domain.City;
import travel.domain.dto.CityDto;
import travel.domain.dto.req.city.AddCityDto;
import travel.domain.dto.req.city.DelCityDto;
import travel.domain.dto.req.city.ModCityDto;
import travel.domain.dto.res.CityResDto;
import travel.domain.dto.res.SingleResultDto;
import travel.repository.CityRepository;
import travel.util.Validation;
import travel.util.helper.enums.StatusCode;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CityService {

    private final CityRepository cityRepository;
    private final Validation validation;

    /** 도시 등록 */
    public CityResDto add(AddCityDto dto) {
        validation.isExistCityName(dto.getFullAddr());
        try {

            City city = City.builder()
                    .addr_1(dto.getAddr_1())
                    .addr_2(dto.getAddr_2())
                    .cityName(dto.getCityName())
                    .fullAddr(dto.getFullAddr())
                    .explanation(dto.getExplanation())
                    .build();
            City savedCity = cityRepository.save(city);

            return new CityResDto(savedCity.getId(), StatusCode.OK.getCode(), StatusCode.OK.getMsg());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new CityResDto(null, StatusCode.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }
    }

    /** 도시 수정 */
    public CityResDto mod(ModCityDto dto) {
        City getCity = validation.isExistCity(dto.getCityId());
        try {
            if (!dto.getFullAddr().equals(getCity.getFullAddr())) {
                validation.isExistCityName(dto.getFullAddr());
            }

            getCity.setAddr_1(dto.getAddr_1());
            getCity.setAddr_2(dto.getAddr_2());
            getCity.setFullAddr(dto.getFullAddr());
            getCity.setCityName(dto.getCityName());
            getCity.setExplanation(dto.getExplanation());

            City savedCity = cityRepository.save(getCity);

            return new CityResDto(savedCity.getId(), StatusCode.OK.getCode(), StatusCode.OK.getMsg());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new CityResDto(dto.getCityId(), StatusCode.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }
    }

    /** 도시 삭제 */
    public CityResDto del(DelCityDto dto) {
        City getCity = validation.isExistCity(dto.getCityId());

        try {
            if(getCity.getTravelList() != null) throw new Exception("해당 도시에 등록된 여행이 존재합니다.");
            cityRepository.deleteById(dto.getCityId());

            return new CityResDto(dto.getCityId(), StatusCode.OK.getCode(), StatusCode.OK.getMsg());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new CityResDto(dto.getCityId(), StatusCode.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }

    }

    /** 도시 조회 */
    public SingleResultDto getCitySingleResult(long id) {
        City getCity = validation.isExistCity(id);// 도시 존재 여부 체크

        try {
            CityDto result = CityDto.builder()
                    .cityId(getCity.getId())
                    .addr_1(getCity.getAddr_1())
                    .addr_2(getCity.getAddr_2())
                    .cityName(getCity.getCityName())
                    .fullAddr(getCity.getFullAddr())
                    .explanation(getCity.getExplanation())
                    .build();

            return new SingleResultDto(StatusCode.OK.getCode(), StatusCode.OK.getMsg(), result);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new SingleResultDto(StatusCode.INTERNAL_SERVER_ERROR.getCode(), e.getMessage(), null);
        }
    }
}
