package travel.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;
import travel.domain.City;
import travel.domain.dto.CityDto;
import travel.domain.dto.req.city.AddCityDto;
import travel.domain.dto.req.city.DelCityDto;
import travel.domain.dto.req.city.ModCityDto;
import travel.domain.dto.res.CityResDto;
import travel.domain.dto.res.SingleResultDto;
import travel.repository.CityRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CityServiceTest {

    @Autowired
    CityService cityService;

    @Autowired
    CityRepository cityRepository;

    @Test
    @Description("city 등록 테스트")
    @Transactional
    void add() {
        AddCityDto city = AddCityDto.builder()
                .addr_1("경기도")
                .addr_2("군포시")
                .cityName("산본")
                .explanation("수리산이 있는 도시.")
                .build();


        CityResDto result = cityService.add(city);

        assertEquals(result.getCode(), 200);

    }

    @Test
    @Description("city 수정 테스트")
    @Transactional
    void mod() {
        AddCityDto city = AddCityDto.builder()
                .addr_1("경기도")
                .addr_2("군포시")
                .cityName("산본")
                .explanation("수리산이 있는 도시.")
                .build();


        CityResDto result = cityService.add(city);
        City savedCity = cityRepository.getById(result.getCityId());

        ModCityDto modCity = ModCityDto.builder()
                .cityId(savedCity.getId())
                .addr_1("경기도")
                .addr_2("의왕시")
                .cityName("왕송호수")
                .fullAddr("경기도 의왕시 왕송호수")
                .explanation("카페가 많은 호수.")
                .build();

        cityService.mod(modCity);
        City getCity = cityRepository.getById(savedCity.getId());

        assertEquals(getCity.getFullAddr(), modCity.getFullAddr());
    }

    @Test
    @Description("city 삭제 테스트")
    @Transactional
    void del() {
        AddCityDto city = AddCityDto.builder()
                .addr_1("경기도")
                .addr_2("군포시")
                .cityName("산본")
                .explanation("수리산이 있는 도시.")
                .build();


        CityResDto result = cityService.add(city);
        City savedCity = cityRepository.getById(result.getCityId());


        DelCityDto dto = new DelCityDto();
        dto.setCityId(savedCity.getId());
        cityService.del(dto);

        assertFalse(cityRepository.existsById(savedCity.getId()));
    }

    @Test
    @Description("city 단건 조회 테스트")
    @Transactional
    void getSingleResult() {
        AddCityDto city = AddCityDto.builder()
                .addr_1("경기도")
                .addr_2("군포시")
                .cityName("산본")
                .explanation("수리산이 있는 도시.")
                .build();


        CityResDto result = cityService.add(city);
        City savedCity = cityRepository.getById(result.getCityId());
        //TODO
//        SingleResultDto singleResult = cityService.getCitySingleResult(savedCity.getId());
//        CityDto ret = (CityDto) singleResult.getResult();
//        assertEquals(ret.getFullAddr(), city.getFullAddr());
    }


}
