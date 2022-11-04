package travel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import travel.domain.City;
import travel.domain.Travel;
import travel.domain.User;
import travel.domain.dto.req.travel.AddTravelDto;
import travel.domain.dto.req.travel.DelTravelDto;
import travel.domain.dto.req.travel.ModTravelDto;
import travel.domain.dto.res.SingleResultDto;
import travel.domain.dto.res.TravelResDto;
import travel.repository.CityRepository;
import travel.repository.TravelRepository;
import travel.repository.UserRepository;
import travel.service.TravelService;

import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TravelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    TravelRepository travelRepository;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TravelService travelService;

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

    private ModTravelDto getModDto(TravelResDto getTravel) {
        return ModTravelDto.builder()
                .travelId(getTravel.getTravelId())
                .title("여행 수정 API 테스트")
                .startDate(LocalDate.of(2022, 11, 05))
                .endDate(LocalDate.of(2022, 11, 07))
                .cityId(saveCity())
                .userId(saveUser())
                .build();
    }

    @Test
    @Description("여행 등록 API 테스트 POST /travel/add")
    void 여행등록_테스트() throws Exception {
        AddTravelDto travel = AddTravelDto.builder()
                .title("여행 등록 API 테스트용 travel")
                .startDate(LocalDate.of(2022, 12, 01))
                .endDate(LocalDate.of(2022, 12, 03))
                .cityId(saveCity())
                .userId(saveUser())
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/travel/add")
                        .content(mapper.writeValueAsString(travel))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    @Description("여행 수정 API 테스트 POST /travel/mod")
    void 여행수정_테스트() throws Exception {
        /** 여행 등록 */
        AddTravelDto travel = AddTravelDto.builder()
                .title("여행 수정 API 테스트용 travel")
                .startDate(LocalDate.of(2022, 12, 01))
                .endDate(LocalDate.of(2022, 12, 03))
                .cityId(saveCity())
                .userId(saveUser())
                .build();

        TravelResDto getTravel = travelService.add(travel);
        ModTravelDto modTravel = getModDto(getTravel);

        ObjectMapper modMapper = new ObjectMapper();
        modMapper.registerModule(new JavaTimeModule());
        mockMvc.perform(MockMvcRequestBuilders
                .post("/travel/mod")
                .content(modMapper.writeValueAsString(modTravel))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        Travel getModTravel = travelRepository.getById(getTravel.getTravelId());

        assertEquals(getModTravel.getTitle(), modTravel.getTitle());
        assertEquals(getModTravel.getStartDate(), modTravel.getStartDate());
        assertEquals(getModTravel.getEndDate(), modTravel.getEndDate());
    }

    @Test
    @Description("여행 삭제 API 테스트 POST /travel/del")
    void 여행삭제_테스트() throws Exception {
        /** 여행 등록 */
        AddTravelDto travel = AddTravelDto.builder()
                .title("여행 삭제 API 테스트용 travel")
                .startDate(LocalDate.of(2022, 12, 01))
                .endDate(LocalDate.of(2022, 12, 03))
                .cityId(saveCity())
                .userId(saveUser())
                .build();
        TravelResDto getTravel = travelService.add(travel);


        DelTravelDto delDto = new DelTravelDto();
        delDto.setTravelId(getTravel.getTravelId());

        ObjectMapper delMapper = new ObjectMapper();
        delMapper.registerModule(new JavaTimeModule());
        mockMvc.perform(MockMvcRequestBuilders
                .post("/travel/del")
                .content(delMapper.writeValueAsString(delDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        assertFalse(travelRepository.existsById(getTravel.getTravelId()));
    }

    @Test
    @Description("여행 단건 조회 API 테스트 GET /travel/single")
    void 여행단건조회_테스트() throws Exception {

        /** 여행 등록 */
        AddTravelDto travel = AddTravelDto.builder()
                .title("여행 단건 조회 API 테스트용 travel")
                .startDate(LocalDate.of(2022, 12, 01))
                .endDate(LocalDate.of(2022, 12, 03))
                .cityId(saveCity())
                .userId(saveUser())
                .build();
        TravelResDto getTravel = travelService.add(travel);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/travel/single?id=" + getTravel.getTravelId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        ObjectMapper resMapper = new ObjectMapper();

        SingleResultDto singleResultDto = resMapper.readValue(contentAsString, SingleResultDto.class);
        HashMap map = (HashMap) singleResultDto.getResult();
        Integer travelId = (Integer) map.get("travelId");

        assertEquals(Long.valueOf(travelId), getTravel.getTravelId());
    }

}
