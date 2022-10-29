package travel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import travel.domain.City;
import travel.domain.Travel;
import travel.domain.User;
import travel.domain.dto.TravelDto;
import travel.domain.dto.res.TravelResDto;
import travel.repository.CityRepository;
import travel.repository.TravelRepository;
import travel.repository.UserRepository;
import travel.util.Validation;
import travel.util.helper.enums.StatusCode;

@Service
@RequiredArgsConstructor
@Transactional
public class TravelService {

    private final TravelRepository travelRepository;
    private final CityRepository cityRepository;
    private final UserRepository userRepository;

    private final Validation validation;

    public TravelResDto travelHandler(TravelDto travelDTO) {
        String type = travelDTO.getType();

        Long travelId = 0L;

        if (type.equals("DEL")) {
            del(travelDTO);
            travelId = travelDTO.getTravelId();
        } else {
            validation.isExistCity(travelDTO.getCityId()); // 도시 체크
            validation.isValidEndDate(travelDTO.getEndDate()); // endDate 체크

            if (type.equals("ADD")) {
                travelId = add(travelDTO);
            } else if (type.equals("MOD")) {
                travelId = mod(travelDTO);
            }
        }

        return setResponse(travelId, StatusCode.SUCCESS);
    }



    public Long add(TravelDto travelDTO) {
        validation.isExistUser(travelDTO.getUserId()); // 유저 체크

        Travel travel = new Travel();
        travel.setTitle(travelDTO.getTitle());
        travel.setStartDate(travelDTO.getStartDate());
        travel.setEndDate(travelDTO.getEndDate());
        travel.setUser(getUser(travelDTO.getUserId()));
        travel.setCity(getCity(travelDTO.getCityId()));
        Travel savedTravel = travelRepository.save(travel);
        return savedTravel.getId();

//        return setResponse(savedTravel.getId(), StatusCode.SUCCESS);
    }

    public Long mod(TravelDto travelDTO) {
        validation.isExistTravel(travelDTO.getTravelId()); // 여행 체크

        Travel getTravel = travelRepository.getById(travelDTO.getTravelId());
        getTravel.setStartDate(travelDTO.getStartDate());
        getTravel.setEndDate(travelDTO.getEndDate());
        getTravel.setTitle(travelDTO.getTitle());
        getTravel.setCity(getCity(travelDTO.getCityId()));
        Travel savedTravel = travelRepository.save(getTravel);
        return savedTravel.getId();

//        return setResponse(savedTravel.getId(), StatusCode.SUCCESS);
    }

    public void del(TravelDto travelDTO) {
        validation.isExistTravel(travelDTO.getTravelId());
        travelRepository.deleteById(travelDTO.getTravelId());

//        return setResponse(travelDTO.getTravelId(), StatusCode.SUCCESS);
    }

    public City getCity(Long cityId) {
        return cityRepository.getById(cityId);
    }

    public User getUser(Long userId) {
        return userRepository.getById(userId);
    }




    public TravelDto getTravel(Long id) {
        validation.isExistTravel(id); // 여행 체크
        Travel getTravel = travelRepository.getById(id);

        return TravelDto.builder()
                .title(getTravel.getTitle())
                .travelId(getTravel.getId())
                .userId(getTravel.getUser().getId())
                .cityId(getTravel.getCity().getId())
                .startDate(getTravel.getStartDate())
                .endDate(getTravel.getEndDate())
                .build();
    }


    public TravelResDto setResponse(Long id, StatusCode code) {
        TravelResDto travelResDto = new TravelResDto();
        travelResDto.setTravelId(id);
        travelResDto.setCode(code.getCode());
        return travelResDto;
    }

}
