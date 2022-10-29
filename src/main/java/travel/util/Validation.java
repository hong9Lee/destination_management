package travel.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import travel.repository.CityRepository;
import travel.repository.TravelRepository;
import travel.repository.UserRepository;
import travel.util.helper.exception.EndDateException;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
@Slf4j
public class Validation {

    private final TravelRepository travelRepository;
    private final CityRepository cityRepository;
    private final UserRepository userRepository;

    public void isValidEndDate(LocalDate endDate) {
        try {
            if (!endDate.isAfter(LocalDate.now())) throw new Exception();
        } catch (Exception e) {
            throw new EndDateException("여행 종료일은 미래만 가능합니다.");
        }
    }

    public void isExistUser(Long userId) {
        try {
            if(!userRepository.existsById(userId)) throw new Exception();
        } catch (Exception e) {
            log.error("유저가 존재하지 않습니다 !!");
            throw new EmptyResultDataAccessException("유저가 존재하지 않습니다. user id를 확인해주세요.", 1);
        }
    }

    public void isExistCity(Long cityId) {
        try {
            if (!cityRepository.existsById(cityId)) throw new Exception();
        } catch (Exception e) {
            log.error("도시가 존재하지 않습니다 !!");
            throw new EmptyResultDataAccessException("도시가 존재하지 않습니다. city id를 확인해주세요.", 1);
        }
    }

    public void isExistTravel(Long travelId) {
        try {
            if(!travelRepository.existsById(travelId)) throw new Exception();
        } catch (Exception e) {
            log.error("여행이 존재하지 않습니다 !!");
            throw new EmptyResultDataAccessException("여행이 존재하지 않습니다. travel id를 확인해주세요.", 1);
        }

    }
}
