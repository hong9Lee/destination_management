package travel.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;
import travel.domain.City;
import travel.domain.Travel;
import travel.domain.User;
import travel.domain.dto.TravelDto;
import travel.domain.dto.req.travel.AddTravelDto;
import travel.domain.dto.req.travel.DelTravelDto;
import travel.domain.dto.req.travel.ModTravelDto;
import travel.domain.dto.res.TravelResDto;
import travel.domain.dto.res.SingleResultDto;
import travel.repository.CityRepository;
import travel.repository.TravelRepository;
import travel.repository.UserRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TravelServiceTest {

    @Autowired
    TravelService travelService;

    @Autowired
    TravelRepository travelRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CityRepository cityRepository;

    long saveUser() {
        User user = new User();
        user.setUserName("testUser_1");

        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    long saveCity() {
        City city = new City();
        city.setAddr_1("충청남도");
        city.setAddr_2("논산시");
        city.setCityName("육군훈련소");
        city.setFullAddr("충청남도 논산시 육군훈련소");
        city.setExplanation("국내 최대규모 육군 훈련소");

        City savedCity = cityRepository.save(city);
        return savedCity.getId();
    }

    @Test
    @Description("travel 등록 테스트")
    void add() {
        AddTravelDto travel = AddTravelDto.builder()
                .title("서해바다")
                .startDate(LocalDate.of(2022, 10, 30))
                .endDate(LocalDate.of(2022, 10, 31))
                .cityId(saveCity())
                .userId(saveUser())
                .build();

        TravelResDto result = travelService.add(travel);

        assertEquals(result.getCode(), 200);
    }

    @Test
    @Description("travel 수정 테스트")
    void mod() {
        AddTravelDto addTravel = AddTravelDto.builder()
                .title("서해바다")
                .startDate(LocalDate.of(2022, 10, 30))
                .endDate(LocalDate.of(2022, 10, 31))
                .cityId(saveCity())
                .userId(saveUser())
                .build();

        travelService.add(addTravel);
        Travel savedTravel = travelRepository.findByTitleAndUserId(addTravel.getTitle(), addTravel.getUserId());

        ModTravelDto modTravel = ModTravelDto.builder()
                .travelId(savedTravel.getId())
                .title("동해바다")
                .startDate(LocalDate.of(2022, 11, 01))
                .endDate(LocalDate.of(2022, 11, 02))
                .cityId(saveCity())
                .userId(saveUser())
                .build();

        travelService.mod(modTravel);
        Travel getTravel = travelRepository.getById(savedTravel.getId());

        assertEquals(getTravel.getTitle(), "동해바다");

    }

    @Test
    @Description("travel 삭제 테스트")
    void del() {
        AddTravelDto addTravel = AddTravelDto.builder()
                .title("서해바다")
                .startDate(LocalDate.of(2022, 11, 29))
                .endDate(LocalDate.of(2022, 11, 30))
                .cityId(saveCity())
                .userId(saveUser())
                .build();

        TravelResDto travel = travelService.add(addTravel);

        DelTravelDto dto = new DelTravelDto();
        dto.setTravelId(travel.getTravelId());
        travelService.del(dto);

        assertFalse(travelRepository.existsById(travel.getTravelId()));
    }

    @Test
    @Description("travel 단건 조회 테스트")
    void getSingleResult() {
        AddTravelDto addTravel = AddTravelDto.builder()
                .title("서해바다")
                .startDate(LocalDate.of(2022, 10, 30))
                .endDate(LocalDate.of(2022, 10, 31))
                .cityId(saveCity())
                .userId(saveUser())
                .build();

        travelService.add(addTravel);
        Travel savedTravel = travelRepository.findByTitleAndUserId(addTravel.getTitle(), addTravel.getUserId());

        SingleResultDto result = travelService.getTravelSingleResult(savedTravel.getId());
        TravelDto ret = (TravelDto) result.getResult();
        assertEquals(ret.getTitle(), "서해바다");
    }

}
