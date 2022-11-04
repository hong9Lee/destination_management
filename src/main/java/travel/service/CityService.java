package travel.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import travel.domain.City;
import travel.domain.Travel;
import travel.domain.User;
import travel.domain.dto.CityDto;
import travel.domain.dto.res.CityResultDto;
import travel.repository.UserRepository;
import travel.util.helper.listener.SearchEvent;
import travel.domain.dto.req.city.AddCityDto;
import travel.domain.dto.req.city.DelCityDto;
import travel.domain.dto.req.city.ModCityDto;
import travel.domain.dto.res.CityResDto;
import travel.domain.dto.res.SingleResultDto;
import travel.repository.CityRepository;
import travel.util.helper.valid.Validation;
import travel.util.helper.enums.StatusCode;

import java.util.*;

import static travel.util.helper.CityListUtils.*;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CityService {

    private final CityRepository cityRepository;
    private final Validation validation;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserRepository userRepository;

    /**
     * 도시 등록
     */
    public CityResDto add(AddCityDto dto) {
        validation.isExistCityName(dto.getFullAddr());
        try {

            City city = cityMapper(dto);
            City savedCity = cityRepository.save(city);

            return new CityResDto(savedCity.getId(), StatusCode.OK.getCode(), StatusCode.OK.getMsg());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new CityResDto(null, StatusCode.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }
    }


    /**
     * 도시 수정
     */
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

    /**
     * 도시 삭제
     */
    public CityResDto del(DelCityDto dto) {
        City getCity = validation.isExistCity(dto.getCityId());

        try {
            if (!getCity.getTravelList().isEmpty()) throw new Exception("해당 도시에 등록된 여행이 존재합니다.");
            cityRepository.deleteById(getCity.getId());

            return new CityResDto(getCity.getId(), StatusCode.OK.getCode(), StatusCode.OK.getMsg());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new CityResDto(dto.getCityId(), StatusCode.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }

    }

    /**
     * 도시 조회
     */
    public SingleResultDto getCitySingleResult(long cityId, long userId) {
        City getCity = validation.isExistCity(cityId);// 도시 존재 여부 체크

        try {
            CityDto result = cityDtoMapper(getCity);
            searchHistoryEventPublisher(cityId, userId);
            return new SingleResultDto(StatusCode.OK.getCode(), StatusCode.OK.getMsg(), result);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new SingleResultDto(StatusCode.INTERNAL_SERVER_ERROR.getCode(), e.getMessage(), null);
        }
    }

    /** search History Event Publisher */
    private void searchHistoryEventPublisher(long cityId, long userId) {
        SearchEvent searchEvent = new SearchEvent(this, userId, cityId);
        applicationEventPublisher.publishEvent(searchEvent);
    }


    /**
     * 사용자별 도시 목록 조회
     */
    public SingleResultDto getCityList(Long userId) {

        try {
            validation.isExistUser(userId);
            User getUser = userRepository.getById(userId);
            List<Travel> travelList = getUser.getTravelList();

            ArrayList<City> travelingResult = new ArrayList<>();
            ArrayList<City> dupList = new ArrayList<>();


            // 여행중인 도시 : 여행 시작일이 가까운 것부터 (중복 허용)
            travelingResult.addAll(getTravelingCityList(travelList));

            // 여행이 예정된 도시 : 여행 시작일이 가까운 것부터
            dupList.addAll(getFutureTravelCityList(travelList));

            // 하루 이내에 등록된 도시 : 가장 최근에 등록한 것부터
            dupList.addAll(getRecentRegistrationCityList(travelList));

            // 최근 일주일 이내에 한 번 이상 조회된 도시 : 가장 최근에 조회한 것부터
            dupList.addAll(getSearchList(getUser, travelList));

            // 위의 조건에 해당하지 않는 모든 도시 : 무작위
            dupList.addAll(getCityByTravel(travelList));


            ArrayList<CityResultDto> resDto = setResultDto(travelingResult, dupList);

            // 상위 10개만 노출
            if (resDto.size() > 10) {
                return new SingleResultDto(StatusCode.OK.getCode(), StatusCode.OK.getMsg(), resDto.subList(0, 10));
            } else {
                return new SingleResultDto(StatusCode.OK.getCode(), StatusCode.OK.getMsg(), resDto);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return new SingleResultDto(StatusCode.INTERNAL_SERVER_ERROR.getCode(), e.getMessage(), null);
        }
    }




}
