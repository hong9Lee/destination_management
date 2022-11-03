package travel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.aspectj.lang.annotation.Before;
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
import org.springframework.web.filter.CharacterEncodingFilter;
import travel.domain.City;
import travel.domain.User;
import travel.domain.dto.req.city.AddCityDto;
import travel.domain.dto.req.city.DelCityDto;
import travel.domain.dto.req.city.ModCityDto;
import travel.domain.dto.req.travel.AddTravelDto;
import travel.domain.dto.res.CityResDto;
import travel.domain.dto.res.SingleResultDto;
import travel.repository.CityRepository;
import travel.repository.UserRepository;

import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

    private AddCityDto givenAddCityDto() {
        return AddCityDto.builder()
                .addr_1("충청북도")
                .addr_2("청주시")
                .cityName("백곡저수지")
                .fullAddr("충청북도 청주시 백곡저수지")
                .explanation("계곡형 저수지")
                .build();
    }

    private AddCityDto givenAddCityDto2() {
        return AddCityDto.builder()
                .addr_1("충청북도")
                .addr_2("청주시")
                .cityName("명암저수지")
                .fullAddr("충청북도 청주시 명암저수지")
                .explanation("명암 보트장에서 레져를 즐길 수 있는 저수지")
                .build();
    }

    private ModCityDto givenModCityDto(Long cityId) {
        return ModCityDto.builder()
                .cityId(cityId)
                .addr_1("경기도")
                .addr_2("의왕시")
                .cityName("왕송호수")
                .fullAddr("경기도 의왕시 왕송호수")
                .explanation("카페가 많은 호수.")
                .build();
    }

    @Test
    @Description("도시 등록 API 테스트 POST /city/add")
    void 도시등록_테스트() throws Exception {
        AddCityDto city = givenAddCityDto();

        ObjectMapper mapper = new ObjectMapper();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/city/add")
                        .content(mapper.writeValueAsString(city))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        ObjectMapper resMapper = new ObjectMapper();

        CityResDto CityResDto = resMapper.readValue(contentAsString, CityResDto.class);
        Long cityId = CityResDto.getCityId();
        City getCity = cityRepository.getById(cityId);
        assertEquals(getCity.getFullAddr(), city.getFullAddr());
    }

    @Test
    @Description("도시 수정 API 테스트 POST /city/mod")
    void 도시수정_테스트() throws Exception {
        AddCityDto city = givenAddCityDto();

        ObjectMapper mapper = new ObjectMapper();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/city/add")
                        .content(mapper.writeValueAsString(city))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        ObjectMapper resMapper = new ObjectMapper();

        CityResDto CityResDto = resMapper.readValue(contentAsString, CityResDto.class);
        Long cityId = CityResDto.getCityId();

        ModCityDto modCity = givenModCityDto(cityId);

        ObjectMapper modMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/city/mod")
                        .content(modMapper.writeValueAsString(modCity))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        City getCity = cityRepository.getById(cityId);
        assertEquals(getCity.getFullAddr(), modCity.getFullAddr());
    }

    @Test
    @Description("도시 삭제 API 테스트 POST /city/del")
    void 도시삭제_테스트() throws Exception {
        AddCityDto city = givenAddCityDto2();

        ObjectMapper mapper = new ObjectMapper();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/city/add")
                        .content(mapper.writeValueAsString(city))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        ObjectMapper resMapper = new ObjectMapper();

        CityResDto CityResDto = resMapper.readValue(contentAsString, CityResDto.class);
        Long cityId = CityResDto.getCityId();




        DelCityDto delDto = new DelCityDto();
        delDto.setCityId(cityId);

        ObjectMapper delMapper = new ObjectMapper();
        delMapper.registerModule(new JavaTimeModule());
        mockMvc.perform(MockMvcRequestBuilders
                .post("/city/del")
                .content(delMapper.writeValueAsString(delDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        assertFalse(cityRepository.existsById(cityId));
    }

    @Test
    @Description("도시 삭제 API 테스트 (지정된 여행이 있는 경우) POST /city/del")
    void 도시삭제_지정된여행_테스트() throws Exception {
        AddCityDto city = givenAddCityDto();

        ObjectMapper mapper = new ObjectMapper();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/city/add")
                        .content(mapper.writeValueAsString(city))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        ObjectMapper resMapper = new ObjectMapper();

        CityResDto CityResDto = resMapper.readValue(contentAsString, CityResDto.class);
        long cityId = CityResDto.getCityId();


        AddTravelDto travel = AddTravelDto.builder()
                .title("여행 등록 API 테스트용 travel")
                .startDate(LocalDate.of(2022, 12, 01))
                .endDate(LocalDate.of(2022, 12, 03))
                .cityId(cityId)
                .userId(saveUser())
                .build();

        ObjectMapper travelMapper = new ObjectMapper();
        travelMapper.registerModule(new JavaTimeModule());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/travel/add")
                        .content(travelMapper.writeValueAsString(travel))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        DelCityDto delDto = new DelCityDto();
        delDto.setCityId(cityId);

        ObjectMapper delMapper = new ObjectMapper();
        delMapper.registerModule(new JavaTimeModule());
        MvcResult mvcDelResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/city/del")
                .content(delMapper.writeValueAsString(delDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isInternalServerError()).andReturn();


        String contents = mvcDelResult.getResponse().getContentAsString();
        ObjectMapper delResultMapper = new ObjectMapper();

        CityResDto resDto = delResultMapper.readValue(contents, CityResDto.class);
        String msg = resDto.getMsg();

        assertEquals(msg, "해당 도시에 등록된 여행이 존재합니다.");
    }

    @Test
    @Description("도시 단건조회 API 테스트 GET /city/single")
    void 도시단건조회_테스트() throws Exception {
        long userId = saveUser();
        AddCityDto city = givenAddCityDto();

        ObjectMapper mapper = new ObjectMapper();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/city/add")
                        .content(mapper.writeValueAsString(city))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        ObjectMapper resMapper = new ObjectMapper();

        CityResDto CityResDto = resMapper.readValue(contentAsString, CityResDto.class);
        Long cityId = CityResDto.getCityId();




        MvcResult mvcSingleResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/city/single?city_id=" + cityId + "&user_id=" + userId)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        String contents = mvcSingleResult.getResponse().getContentAsString();
        ObjectMapper resultMapper = new ObjectMapper();

        SingleResultDto singleResultDto = resultMapper.readValue(contents, SingleResultDto.class);
        HashMap map = (HashMap) singleResultDto.getResult();
        Integer resultCityId = (Integer) map.get("cityId");

        assertEquals(Long.valueOf(resultCityId), cityId);
    }


    @Test
    @Description("도시 리스트 조회 API 테스트 GET /city/single")
    void 도시리스트조회_테스트() throws Exception {



    }




}
