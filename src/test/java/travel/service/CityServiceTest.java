package travel.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;
import travel.domain.City;
import travel.domain.User;
import travel.domain.dto.CityDto;
import travel.domain.dto.req.city.AddCityDto;
import travel.domain.dto.req.city.DelCityDto;
import travel.domain.dto.req.city.ModCityDto;
import travel.domain.dto.res.CityResDto;
import travel.domain.dto.res.SingleResultDto;
import travel.repository.CityRepository;
import travel.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CityServiceTest {

    @Autowired
    CityService cityService;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    UserRepository userRepository;

    long saveUser() {
        User user = new User();
        user.setUserName("testUser_1");

        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    @Test
    @Description("city 등록 테스트")
    @Transactional
    void add() {
        AddCityDto city = AddCityDto.builder()
                .addr_1("경기도")
                .addr_2("의왕시")
                .cityName("왕송호수")
                .fullAddr("경기도 의왕시 왕송호수")
                .explanation("카페가 많은 호수.")
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
                .addr_2("의왕시")
                .cityName("백운호수")
                .explanation("카페가 많은 호수.")
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
                .addr_2("의왕시")
                .cityName("왕송호수")
                .fullAddr("경기도 의왕시 왕송호수")
                .explanation("카페가 많은 호수.")
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
        long userId = saveUser();

        AddCityDto city = AddCityDto.builder()
                .addr_1("충청북도")
                .addr_2("청주시")
                .cityName("백곡저수지")
                .fullAddr("충청북도 청주시 백곡저수지")
                .explanation("계곡형 저수지")
                .build();

        CityResDto result = cityService.add(city);
        City savedCity = cityRepository.getById(result.getCityId());
        SingleResultDto singleResult = cityService.getCitySingleResult(savedCity.getId(), userId);
        CityDto ret = (CityDto) singleResult.getResult();
        assertEquals(ret.getFullAddr(), city.getFullAddr());
    }

    @Test
    @Description("하루 이내에 등록된 도시 테스트")
    @Transactional
    void testCreatedAt() {
        LocalDateTime time1 = LocalDateTime.of(2022, 10, 27, 1, 3, 12);
        LocalDateTime time2 = LocalDateTime.of(2022, 10, 28, 10, 12, 42);

        long between = ChronoUnit.DAYS.between(time2.withHour(0).withMinute(0).withSecond(0), time1.withHour(0).withMinute(0).withSecond(0));
        assertEquals(between, -1);
    }


}
