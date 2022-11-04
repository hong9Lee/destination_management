package travel.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
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
import travel.domain.dto.req.city.AddCityDto;
import travel.domain.dto.req.city.DelCityDto;
import travel.domain.dto.req.city.ModCityDto;
import travel.domain.dto.req.travel.AddTravelDto;
import travel.domain.dto.res.CityResDto;
import travel.domain.dto.res.CityResultDto;
import travel.domain.dto.res.SingleResultDto;
import travel.domain.dto.res.TravelResDto;
import travel.repository.CityRepository;
import travel.repository.TravelRepository;
import travel.repository.UserRepository;
import travel.service.CityService;
import travel.service.TravelService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

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

    @Autowired
    CityService cityService;

    @Autowired
    TravelService travelService;

    @Autowired
    TravelRepository travelRepository;

    private long saveUser() {
        User user = new User();
        user.setUserName("testUser_1");

        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    private long saveCity1() {
        AddCityDto addCityDto = givenAddCityDto();
        CityResDto addCity = cityService.add(addCityDto);
        return addCity.getCityId();
    }

    private long saveCity2() {
        AddCityDto addCityDto = givenAddCityDto2();
        CityResDto addCity = cityService.add(addCityDto);
        return addCity.getCityId();
    }

    private long saveCity3() {
        AddCityDto addCityDto = givenAddCityDto3();
        CityResDto addCity = cityService.add(addCityDto);
        return addCity.getCityId();
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
                .addr_1("전라북도")
                .addr_2("익산시")
                .cityName("사적 익산 왕궁리 유적")
                .fullAddr("전라북도 익산시 사적 익산 왕궁리 유적")
                .explanation("후백제 견훤의 도읍설이 전해지는 유적")
                .build();
    }

    private AddCityDto givenAddCityDto3() {
        return AddCityDto.builder()
                .addr_1("경상북도")
                .addr_2("포항시")
                .cityName("남구")
                .fullAddr("경상북도 포항시 남구")
                .explanation("호미곶 해맞이로")
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
        long cityId = saveCity1();

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
        long cityId = saveCity2();

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
        long cityId = saveCity1();

        AddTravelDto travel = AddTravelDto.builder()
                .title("여행 등록 API 테스트용 travel")
                .startDate(LocalDate.of(2022, 12, 01))
                .endDate(LocalDate.of(2022, 12, 03))
                .cityId(cityId)
                .userId(saveUser())
                .build();
        travelService.add(travel);

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
        long cityId = saveCity1();

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
    @Description("GET /city/city-list")
    @DisplayName("도시 리스트 조회 API 테스트 (여행중인 도시 : 여행 시작일이 가까운 것부터 [중복가능])")
    void 도시리스트조회_여행중인도시_테스트() throws Exception {

        // <editor-fold desc="도시, 유저, 여행 생성 block">

        // 도시 생성
        long cityId1 = saveCity1();
        long cityId2 = saveCity2();
        long cityId3 = saveCity3();

        // 유저 생성
        long userId = saveUser();

        // 여행 생성
        LocalDateTime now = LocalDateTime.now();


        // 1. 여행중인 도시 - 여행기간 : 2일전 ~ 내일
        LocalDate past1 = now.minusDays(2).toLocalDate();
        LocalDate future1 = now.plusDays(1).toLocalDate();

        AddTravelDto travel1 = AddTravelDto.builder()
                .title("여행 테스트 1")
                .startDate(past1)
                .endDate(future1)
                .cityId(cityId1)
                .userId(userId)
                .build();

        travelService.add(travel1);

        // 2. 여행중인 도시 - 여행기간 : 5일전 ~ 내일모레
        LocalDate past2 = now.minusDays(5).toLocalDate();
        LocalDate future2 = now.plusDays(2).toLocalDate();

        AddTravelDto travel2 = AddTravelDto.builder()
                .title("여행 테스트 2")
                .startDate(past2)
                .endDate(future2)
                .cityId(cityId2)
                .userId(userId)
                .build();

        travelService.add(travel2);


        // 3. 여행중인 도시 - 여행기간 : 1일전 ~ 3일후
        LocalDate past3 = now.minusDays(1).toLocalDate();
        LocalDate future3 = now.plusDays(3).toLocalDate();

        AddTravelDto travel3 = AddTravelDto.builder()
                .title("여행 테스트 3")
                .startDate(past3)
                .endDate(future3)
                .cityId(cityId3)
                .userId(userId)
                .build();

        travelService.add(travel3);

        // 4. 여행중인 도시 - 여행기간 : 1일전 ~ 5일후
        LocalDate past4 = now.minusDays(1).toLocalDate();
        LocalDate future4 = now.plusDays(5).toLocalDate();

        AddTravelDto travel4 = AddTravelDto.builder()
                .title("여행 테스트 4")
                .startDate(past4)
                .endDate(future4)
                .cityId(cityId3)
                .userId(userId)
                .build();

        travelService.add(travel4);

        // </editor-fold>

        List<CityResultDto> result = getCityList(userId);

        /** 여행 시작일 기준 정렬 ( 여행 시작일 : 2일전 -> 2번째로 출력 )
         * 1. 여행기간 : 2일전 ~ 내일,
         * fullAddr :  충청북도 청주시 백곡저수지 */
        assertEquals(result.get(1).getFullAddr(), "충청북도 청주시 백곡저수지");
        assertEquals(result.get(1).getId(), cityId1);
        assertEquals(result.get(1).getIsTraveling(), "Y");

        /** 여행 시작일 기준 정렬 ( 여행 시작일 : 5일전 -> 1번째로 출력 )
         * 2. 여행기간 : 여행기간 : 5일전 ~ 내일모레,
         * fullAddr :  전라북도 익산시 사적 익산 왕궁리 유적 */
        assertEquals(result.get(0).getFullAddr(), "전라북도 익산시 사적 익산 왕궁리 유적");
        assertEquals(result.get(0).getId(), cityId2);
        assertEquals(result.get(0).getIsTraveling(), "Y");

        /** 여행 시작일 기준 정렬 ( 여행 시작일 : 1일전 -> 3번째로 출력 )
         * 3. 여행기간 : 여행기간 : 1일전 ~ 3일후,
         * fullAddr :  경상북도 포항시 남구 */
        assertEquals(result.get(2).getFullAddr(), "경상북도 포항시 남구");
        assertEquals(result.get(2).getId(), cityId3);
        assertEquals(result.get(2).getIsTraveling(), "Y");

        /** 여행 시작일 기준 정렬 - 여행중인 도시는 중복 허용 ( 여행 시작일 : 1일전 -> 4번째로 출력 )
         * 4. 여행기간 : 여행기간 : 1일전 ~ 5일후,
         * fullAddr :  경상북도 포항시 남구 (중복 체크) */
        assertEquals(result.get(3).getFullAddr(), "경상북도 포항시 남구");
        assertEquals(result.get(3).getId(), cityId3);
        assertEquals(result.get(3).getIsTraveling(), "Y");
    }


    @Test
    @Description("GET /city/city-list")
    @DisplayName("도시 리스트 조회 API 테스트 (여행이 예정된 도시 : 여행 시작일이 가까운 것부터 [중복제거])")
    void 도시리스트조회_여행예정도시_테스트() throws Exception {

        // <editor-fold desc="도시, 유저, 여행 생성 block">

        // 도시 생성
        long cityId1 = saveCity1();
        long cityId2 = saveCity2();
        long cityId3 = saveCity3();

        // 유저 생성
        long userId = saveUser();

        // 여행 생성
        LocalDateTime now = LocalDateTime.now();

        // 1. 여행예정 도시 - 여행기간 : 2일전 ~ 10일후
        LocalDate startDate1 = now.plusDays(2).toLocalDate();
        LocalDate endDate1 = now.plusDays(10).toLocalDate();

        AddTravelDto travel1 = AddTravelDto.builder()
                .title("여행 예정 도시테스트 1")
                .startDate(startDate1)
                .endDate(endDate1)
                .cityId(cityId1)
                .userId(userId)
                .build();
        travelService.add(travel1);

        // 2. 여행예정 도시 - 여행기간 : 1일후 ~ 10후
        LocalDate startDate2 = now.plusDays(1).toLocalDate();
        LocalDate endDate2 = now.plusDays(10).toLocalDate();

        AddTravelDto travel2 = AddTravelDto.builder()
                .title("여행 예정 도시테스트 2")
                .startDate(startDate2)
                .endDate(endDate2)
                .cityId(cityId2)
                .userId(userId)
                .build();
        travelService.add(travel2);


        // 3. 여행예정 도시 - 여행기간 : 4일후 ~ 10후
        LocalDate startDate3 = now.plusDays(4).toLocalDate();
        LocalDate endDate3 = now.plusDays(10).toLocalDate();

        AddTravelDto travel3 = AddTravelDto.builder()
                .title("여행 예정 도시테스트 3")
                .startDate(startDate3)
                .endDate(endDate3)
                .cityId(cityId3)
                .userId(userId)
                .build();
        travelService.add(travel3);

        // 4. 여행예정 도시 - 여행기간 : 8일후 ~ 10일후
        LocalDate startDate4 = now.plusDays(8).toLocalDate();
        LocalDate endDate4 = now.plusDays(10).toLocalDate();

        AddTravelDto travel4 = AddTravelDto.builder()
                .title("여행 예정 도시테스트 4")
                .startDate(startDate4)
                .endDate(endDate4)
                .cityId(cityId3)
                .userId(userId)
                .build();
        travelService.add(travel4);

        // </editor-fold>

        /** 도시 목록 조회 */
        List<CityResultDto> result = getCityList(userId);


        /** 여행 시작일 기준 정렬 ( 여행 시작일 : 2일후 -> 2번째로 출력 )
         * 1. 여행기간 : 2일후 ~ 10일후,
         * fullAddr :  충청북도 청주시 백곡저수지 */
        assertEquals(result.get(1).getFullAddr(), "충청북도 청주시 백곡저수지");
        assertEquals(result.get(1).getId(), cityId1);
        assertEquals(result.get(1).getIsTraveling(), "N");

        /** 여행 시작일 기준 정렬 ( 여행 시작일 : 1일후 -> 1번째로 출력 )
         * 2. 여행기간 : 여행기간 : 1일후 ~ 10일후,
         * fullAddr :  전라북도 익산시 사적 익산 왕궁리 유적 */
        assertEquals(result.get(0).getFullAddr(), "전라북도 익산시 사적 익산 왕궁리 유적");
        assertEquals(result.get(0).getId(), cityId2);
        assertEquals(result.get(0).getIsTraveling(), "N");

        /** 여행 시작일 기준 정렬 ( 여행 시작일 : 4일후 -> 3번째로 출력 )
         * 3. 여행기간 : 여행기간 : 4일후 ~ 10일후,
         * fullAddr :  경상북도 포항시 남구 */
        assertEquals(result.get(2).getFullAddr(), "경상북도 포항시 남구");
        assertEquals(result.get(2).getId(), cityId3);
        assertEquals(result.get(2).getIsTraveling(), "N");

        /** 여행 시작일 기준 정렬 - 여행이 예정된 도시는 중복 제거 ( 여행 시작일 : 8일후 -> 출력 안됨 )
         * 4. 여행기간 : 여행기간 : 8일후 ~ 10일후,
         * fullAddr :  경상북도 포항시 남구 (중복 제거) */
        assertEquals(-1, result.indexOf(3));
    }


    @Test
    @Description("GET /city/city-list")
    @DisplayName("도시 리스트 조회 API 테스트 ( 최근 일주일 이내에 한 번 이상 조회된 도시 : 가장 최근에 조회한 것부터[중복제거])")
    void 도시리스트조회_하루이내등록_테스트() throws Exception {
        // <editor-fold desc="도시, 유저, 여행 생성 block">

        // 도시 생성
        long cityId2 = saveCity2();

        // 유저 생성
        long userId = saveUser();

        LocalDateTime now = LocalDateTime.now();

        // 여행 생성
        // 1. 여행 생성일 - 1일 10초 전 생성
        AddTravelDto travelDto2 = AddTravelDto.builder()
                .title("하루 이내 등록 여행 테스트 2")
                .startDate(now.minusDays(10).toLocalDate())
                .endDate(now.minusDays(9).toLocalDate())
                .cityId(cityId2)
                .userId(userId)
                .build();
        TravelResDto travelResult2 = travelService.add(travelDto2);
        Travel travel2 = travelRepository.getById(travelResult2.getTravelId());
        travel2.setCreatedAt(travel2.getCreatedAt().minusDays(1).minusSeconds(10));

        travelRepository.save(travel2);

        // 2. 여행 생성일 - 1일 2초 전 생성
        AddTravelDto travelDto3 = AddTravelDto.builder()
                .title("하루 이내 등록 여행 테스트 3")
                .startDate(now.minusDays(10).toLocalDate())
                .endDate(now.minusDays(9).toLocalDate())
                .cityId(cityId2)
                .userId(userId)
                .build();
        TravelResDto travelResult3 = travelService.add(travelDto3);
        Travel travel3 = travelRepository.getById(travelResult3.getTravelId());
        travel3.setCreatedAt(travel3.getCreatedAt().minusDays(1).minusSeconds(2));

        travelRepository.save(travel3);
        // </editor-fold>

        /** 도시 목록 조회 */
        List<CityResultDto> result = getCityList(userId);

        /** 등록일 기준 정렬 [중복 제거]
         * fullAddr :  전라북도 익산시 사적 익산 왕궁리 유적 */
        assertEquals(result.get(0).getFullAddr(), "전라북도 익산시 사적 익산 왕궁리 유적");
        assertEquals(result.get(0).getId(), cityId2);
        assertEquals(result.get(0).getIsTraveling(), "N");
        assertEquals(1, result.size());

    }


    @Test
    @Description("GET /city/city-list")
    @DisplayName("도시 리스트 조회 API 테스트 (최근 일주일 이내에 한 번 이상 조회된 도시 : 가장 최근에 조회한 것부터 [중복제거])")
    void 도시리스트조회_최근조회_테스트() throws Exception {
        // <editor-fold desc="도시, 유저, 여행 생성부분">

        // 도시 생성
        long cityId1 = saveCity1();
        long cityId2 = saveCity2();
        long cityId3 = saveCity3();

        // 유저 생성
        long userId = saveUser();


        // </editor-fold>

        /** 첫번째 조회
         *  도시명 : 전라북도 익산시 사적 익산 왕궁리 유적 */
        mockMvc.perform(MockMvcRequestBuilders
                .get("/city/single?city_id=" + cityId2 + "&user_id=" + userId)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        /** 두번째 조회
         *  도시명 : 충청북도 청주시 백곡저수지 */
        mockMvc.perform(MockMvcRequestBuilders
                .get("/city/single?city_id=" + cityId1 + "&user_id=" + userId)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        /** 세번째 조회
         *  도시명 : 경상북도 포항시 남구 */
        mockMvc.perform(MockMvcRequestBuilders
                .get("/city/single?city_id=" + cityId3 + "&user_id=" + userId)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        /** 네번째 조회
         *  도시명 : 전라북도 익산시 사적 익산 왕궁리 유적 */
        mockMvc.perform(MockMvcRequestBuilders
                .get("/city/single?city_id=" + cityId2 + "&user_id=" + userId)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());


        /** 도시 목록 조회 */
        List<CityResultDto> result = getCityList(userId);

        /** 가장 최근 조회 기준 정렬 ( 2번째로 출력 )
         * fullAddr :  충청북도 청주시 백곡저수지 */
        assertEquals(result.get(1).getFullAddr(), "충청북도 청주시 백곡저수지");
        assertEquals(result.get(1).getId(), cityId1);
        assertEquals(result.get(1).getIsTraveling(), "N");

        /** 가장 최근 조회 기준 정렬 ( 1번째로 출력 )
         * fullAddr :  전라북도 익산시 사적 익산 왕궁리 유적 */
        assertEquals(result.get(0).getFullAddr(), "전라북도 익산시 사적 익산 왕궁리 유적");
        assertEquals(result.get(0).getId(), cityId2);
        assertEquals(result.get(0).getIsTraveling(), "N");

        /** 가장 최근 조회 기준 정렬 ( 3번째로 출력 )
         * fullAddr :  경상북도 포항시 남구 */
        assertEquals(result.get(2).getFullAddr(), "경상북도 포항시 남구");
        assertEquals(result.get(2).getId(), cityId3);
        assertEquals(result.get(2).getIsTraveling(), "N");

        /** 가장 최근 조회 기준 정렬 ( 중복제거 : 출력 안됨 )
         * fullAddr :  전라북도 익산시 사적 익산 왕궁리 유적 */
        assertEquals(-1, result.indexOf(3));
    }


    @Test
    @Description("GET /city/city-list")
    @DisplayName("도시 리스트 조회 API 테스트 ( 조건에 해당하지 않는 모든 도시 : 무작위 )")
    void 도시리스트조회_else_테스트() throws Exception {
        // <editor-fold desc="도시, 유저, 여행 생성 block">

        // 도시 생성
        long cityId1 = saveCity1();
        long cityId2 = saveCity2();

        // 유저 생성
        long userId = saveUser();

        LocalDateTime now = LocalDateTime.now();

        // 여행 생성
        // 1. 여행 생성일 - 10일전 생성
        AddTravelDto travelDto1 = AddTravelDto.builder()
                .title("하루 이내 등록 여행 테스트 1")
                .startDate(now.minusDays(10).toLocalDate())
                .endDate(now.minusDays(9).toLocalDate())
                .cityId(cityId1)
                .userId(userId)
                .build();
        TravelResDto travelResult1 = travelService.add(travelDto1);
        Travel travel1 = travelRepository.getById(travelResult1.getTravelId());
        travel1.setCreatedAt(travel1.getCreatedAt().minusDays(10));

        travelRepository.save(travel1);

        // 2. 여행 생성일 - 12일전 생성
        AddTravelDto travelDto2 = AddTravelDto.builder()
                .title("하루 이내 등록 여행 테스트 2")
                .startDate(now.minusDays(10).toLocalDate())
                .endDate(now.minusDays(3).toLocalDate())
                .cityId(cityId2)
                .userId(userId)
                .build();
        TravelResDto travelResult2 = travelService.add(travelDto2);
        Travel travel2 = travelRepository.getById(travelResult2.getTravelId());
        travel2.setCreatedAt(travel2.getCreatedAt().minusDays(12));

        travelRepository.save(travel2);

        // 3. 여행 생성일 - 6일전 생성
        AddTravelDto travelDto3 = AddTravelDto.builder()
                .title("하루 이내 등록 여행 테스트 3")
                .startDate(now.minusDays(12).toLocalDate())
                .endDate(now.minusDays(9).toLocalDate())
                .cityId(cityId2)
                .userId(userId)
                .build();
        TravelResDto travelResult3 = travelService.add(travelDto3);
        Travel travel3 = travelRepository.getById(travelResult3.getTravelId());
        travel3.setCreatedAt(travel3.getCreatedAt().minusDays(6));

        travelRepository.save(travel3);
        // </editor-fold>


        /** 도시 목록 조회 */
        List<CityResultDto> result = getCityList(userId);

        /** 모든 조건에 해당하지 않는 경우 (무작위 출력 [중복제거])
         * fullAddr :  충청북도 청주시 백곡저수지 */
        assertEquals(2, result.size());

        assertEquals(result.get(0).getFullAddr(), "충청북도 청주시 백곡저수지");
        assertEquals(result.get(0).getId(), cityId1);
        assertEquals(result.get(0).getIsTraveling(), "N");

        /** 가장 최근 조회 기준 정렬 ( 1번째로 출력 )
         * fullAddr :  전라북도 익산시 사적 익산 왕궁리 유적 */
        assertEquals(result.get(1).getFullAddr(), "전라북도 익산시 사적 익산 왕궁리 유적");
        assertEquals(result.get(1).getId(), cityId2);
        assertEquals(result.get(1).getIsTraveling(), "N");
    }


    @Test
    @Description("GET /city/city-list")
    @DisplayName("도시 리스트 조회 API 테스트 ( 상위 10개 출력 )")
    void 도시리스트조회_결과갯수_테스트() throws Exception {
        // <editor-fold desc="도시, 유저, 여행 생성 block">

        // 도시 생성
        long cityId1 = saveCity1();
        long cityId2 = saveCity2();
        long cityId3 = saveCity3();

        // 유저 생성
        long userId = saveUser();

        // 여행 생성
        LocalDateTime now = LocalDateTime.now();


        // 1. 여행중인 도시 - 여행기간 : 2일전 ~ 내일
        LocalDate past1 = now.minusDays(2).toLocalDate();
        LocalDate future1 = now.plusDays(1).toLocalDate();

        AddTravelDto travel1 = AddTravelDto.builder()
                .title("여행 테스트 1")
                .startDate(past1)
                .endDate(future1)
                .cityId(cityId1)
                .userId(userId)
                .build();

        travelService.add(travel1);

        // 2. 여행중인 도시 - 여행기간 : 5일전 ~ 내일모레
        LocalDate past2 = now.minusDays(5).toLocalDate();
        LocalDate future2 = now.plusDays(2).toLocalDate();

        AddTravelDto travel2 = AddTravelDto.builder()
                .title("여행 테스트 2")
                .startDate(past2)
                .endDate(future2)
                .cityId(cityId2)
                .userId(userId)
                .build();

        travelService.add(travel2);


        // 3. 여행중인 도시 - 여행기간 : 1일전 ~ 3일후
        LocalDate past3 = now.minusDays(1).toLocalDate();
        LocalDate future3 = now.plusDays(3).toLocalDate();

        AddTravelDto travel3 = AddTravelDto.builder()
                .title("여행 테스트 3")
                .startDate(past3)
                .endDate(future3)
                .cityId(cityId3)
                .userId(userId)
                .build();

        travelService.add(travel3);

        // 4. 여행중인 도시 - 여행기간 : 1일전 ~ 5일후
        LocalDate past4 = now.minusDays(1).toLocalDate();
        LocalDate future4 = now.plusDays(5).toLocalDate();

        AddTravelDto travel4 = AddTravelDto.builder()
                .title("여행 테스트 4")
                .startDate(past4)
                .endDate(future4)
                .cityId(cityId3)
                .userId(userId)
                .build();

        travelService.add(travel4);
        travelService.add(travel4);
        travelService.add(travel4);
        travelService.add(travel4);
        travelService.add(travel4);
        travelService.add(travel4);
        travelService.add(travel4);
        travelService.add(travel4);
        travelService.add(travel4);

        // </editor-fold>

        /** 도시 목록 조회 */
        List<CityResultDto> result = getCityList(userId);

        assertEquals(10, result.size());
    }

    /** 도시 목록 조회 메서드 */
    private List<CityResultDto> getCityList(long userId) throws Exception {
        MvcResult mvcSingleResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/city/city-list?id=" + userId)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        String contents = mvcSingleResult.getResponse().getContentAsString();
        ObjectMapper resultMapper = new ObjectMapper();

        SingleResultDto resDto = resultMapper.readValue(contents, SingleResultDto.class);
        return resultMapper.convertValue(resDto.getResult(), new TypeReference<>() {});
    }


}
