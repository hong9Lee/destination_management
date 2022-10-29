package travel.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;
import travel.domain.Travel;
import travel.domain.dto.TravelDto;
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
        TravelDto travel = TravelDto.builder()
                .type("ADD")
                .title("서해바다")
                .startDate(LocalDate.of(2022, 10, 30))
                .endDate(LocalDate.of(2022, 10, 31))
                .cityId(1L)
                .userId(1L)
                .build();

        Long travelId = travelService.add(travel);
        Travel getTravel = travelRepository.getById(travelId);


        assertEquals(travel.getTitle(), getTravel.getTitle());
    }


}
