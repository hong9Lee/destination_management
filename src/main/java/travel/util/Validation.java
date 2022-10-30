package travel.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import travel.domain.City;
import travel.domain.Travel;
import travel.domain.User;
import travel.repository.CityRepository;
import travel.repository.TravelRepository;
import travel.repository.UserRepository;
import travel.util.helper.exception.ExistCityException;

@RequiredArgsConstructor
@Component
@Slf4j
public class Validation {

    private final TravelRepository travelRepository;
    private final CityRepository cityRepository;
    private final UserRepository userRepository;

    public User isExistUser(Long userId) {
        try {
            if(!userRepository.existsById(userId)) throw new Exception();
            return userRepository.getById(userId);
        } catch (Exception e) {
            log.error("유저가 존재하지 않습니다 !!");
            throw new EmptyResultDataAccessException("유저가 존재하지 않습니다. user id를 확인해주세요.", 1);
        }
    }

    public City isExistCity(Long cityId) {
        try {
            if (!cityRepository.existsById(cityId)) throw new Exception();
            return cityRepository.getById(cityId);
        } catch (Exception e) {
            log.error("도시가 존재하지 않습니다 !!");
            throw new EmptyResultDataAccessException("도시가 존재하지 않습니다. city id를 확인해주세요.", 1);
        }
    }

    public Travel isExistTravel(Long travelId) {
        try {
            if(!travelRepository.existsById(travelId)) throw new Exception();
            return travelRepository.getById(travelId);
        } catch (Exception e) {
            log.error("여행이 존재하지 않습니다 !!");
            throw new EmptyResultDataAccessException("여행이 존재하지 않습니다. travel id를 확인해주세요.", 1);
        }
    }

    public void isExistCityName(String fullAddr) {
        try {
            if (cityRepository.existsByFullAddr(fullAddr)) throw new Exception();
        } catch (Exception e) {
            log.error("이미 등록되어 있는 도시 입니다. 주소를 확인해 주세요.");
            throw new ExistCityException("이미 등록되어 있는 도시 입니다. 주소를 확인해 주세요.");
        }

    }
}
