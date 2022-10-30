package travel.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;
import travel.domain.Travel;
import travel.domain.dto.req.AddTravelDto;
import travel.domain.dto.req.DelTravelDto;
import travel.domain.dto.req.ModTravelDto;
import travel.domain.dto.res.TravelResDto;
import travel.domain.dto.res.TravelSingleResultDto;
import travel.repository.TravelRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TravelServiceTest {

    @Autowired
    TravelService travelService;

    @Autowired
    TravelRepository travelRepository;

    @Test
    @Description("travel 등록 테스트")
    @Transactional
    void add() {
        AddTravelDto travel = AddTravelDto.builder()
                .title("서해바다")
                .startDate(LocalDate.of(2022, 10, 30))
                .endDate(LocalDate.of(2022, 10, 31))
                .cityId(1L)
                .userId(1L)
                .build();

        TravelResDto result = travelService.add(travel);

        assertEquals(result.getCode(), 200);
    }

    @Test
    @Description("travel 수정 테스트")
    @Transactional
    void mod() {

        AddTravelDto addTravel = AddTravelDto.builder()
                .title("서해바다")
                .startDate(LocalDate.of(2022, 10, 30))
                .endDate(LocalDate.of(2022, 10, 31))
                .cityId(1L)
                .userId(1L)
                .build();

        travelService.add(addTravel);
        Travel savedTravel = travelRepository.findByTitleAndUserId(addTravel.getTitle(), addTravel.getUserId());

        ModTravelDto modTravel = ModTravelDto.builder()
                .travelId(savedTravel.getId())
                .title("동해바다")
                .startDate(LocalDate.of(2022, 11, 01))
                .endDate(LocalDate.of(2022, 11, 02))
                .cityId(1L)
                .userId(1L)
                .build();

        travelService.mod(modTravel);
        Travel getTravel = travelRepository.getById(savedTravel.getId());

        assertEquals(getTravel.getTitle(), "동해바다");

    }

    @Test
    @Description("travel 삭제 테스트")
    @Transactional
    void del() {
        AddTravelDto addTravel = AddTravelDto.builder()
                .title("서해바다")
                .startDate(LocalDate.of(2022, 10, 30))
                .endDate(LocalDate.of(2022, 10, 31))
                .cityId(1L)
                .userId(1L)
                .build();

        travelService.add(addTravel);
        Travel savedTravel = travelRepository.findByTitleAndUserId(addTravel.getTitle(), addTravel.getUserId());

        DelTravelDto dto = new DelTravelDto();
        dto.setTravelId(savedTravel.getId());
        travelService.del(dto);

        assertFalse(travelRepository.existsById(savedTravel.getId()));
    }

    @Test
    @Description("travel 단건 조회 테스트")
    @Transactional
    void getSingleResult() {
        AddTravelDto addTravel = AddTravelDto.builder()
                .title("서해바다")
                .startDate(LocalDate.of(2022, 10, 30))
                .endDate(LocalDate.of(2022, 10, 31))
                .cityId(1L)
                .userId(1L)
                .build();

        travelService.add(addTravel);
        Travel savedTravel = travelRepository.findByTitleAndUserId(addTravel.getTitle(), addTravel.getUserId());

        TravelSingleResultDto result = travelService.getTravelSingleResult(savedTravel.getId());

        assertEquals(result.getResult().getTitle(), "서해바다");
    }

}
