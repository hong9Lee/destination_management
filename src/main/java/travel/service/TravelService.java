package travel.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import travel.domain.Travel;
import travel.domain.dto.TravelDto;
import travel.domain.dto.req.AddTravelDto;
import travel.domain.dto.req.DelTravelDto;
import travel.domain.dto.req.ModTravelDto;
import travel.domain.dto.res.TravelResDto;
import travel.domain.dto.res.TravelSingleResultDto;
import travel.repository.TravelRepository;
import travel.util.Validation;
import travel.util.helper.enums.StatusCode;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TravelService {

    private final TravelRepository travelRepository;
    private final Validation validation;

    /** 여행 등록 메서드 */
    public TravelResDto add(AddTravelDto dto) {
        try {

            Travel travel = new Travel();
            travel.setTitle(dto.getTitle());
            travel.setStartDate(dto.getStartDate());
            travel.setEndDate(dto.getEndDate());
            travel.setUser(validation.isExistUser(dto.getUserId()));
            travel.setCity(validation.isExistCity(dto.getCityId()));

            Travel savedTravel = travelRepository.save(travel);

            return new TravelResDto(savedTravel.getId(), StatusCode.OK.getCode(), StatusCode.OK.getMsg());
        } catch (Exception e){
            log.error(e.getMessage());
            return new TravelResDto(null, StatusCode.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }
    }

    /** 여행 수정 메서드 */
    public TravelResDto mod(ModTravelDto dto) {
        try {

            Travel getTravel = validation.isExistTravel(dto.getTravelId());
            getTravel.setStartDate(dto.getStartDate());
            getTravel.setEndDate(dto.getEndDate());
            getTravel.setTitle(dto.getTitle());
            getTravel.setCity(validation.isExistCity(dto.getCityId()));

            Travel savedTravel = travelRepository.save(getTravel);

            return new TravelResDto(savedTravel.getId(), StatusCode.OK.getCode(), StatusCode.OK.getMsg());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new TravelResDto(dto.getTravelId(), StatusCode.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }
    }

    /** 여행 삭제 메서드 */
    public TravelResDto del(DelTravelDto dto) {
        try {

            validation.isExistTravel(dto.getTravelId()); // 여행 존재유무 체크
            travelRepository.deleteById(dto.getTravelId());

            return new TravelResDto(dto.getTravelId(), StatusCode.OK.getCode(), StatusCode.OK.getMsg());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new TravelResDto(dto.getTravelId(), StatusCode.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }
    }

    /** 여행 단건 조회 */
    public TravelSingleResultDto getTravelSingleResult(Long id) {
        try {

            Travel getTravel = validation.isExistTravel(id); // 여행 체크

            TravelDto result = TravelDto.builder()
                    .title(getTravel.getTitle())
                    .travelId(getTravel.getId())
                    .userId(getTravel.getUser().getId())
                    .cityId(getTravel.getCity().getId())
                    .startDate(getTravel.getStartDate())
                    .endDate(getTravel.getEndDate())
                    .build();
            return new TravelSingleResultDto(StatusCode.OK.getCode(), StatusCode.OK.getMsg(), result);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new TravelSingleResultDto(StatusCode.INTERNAL_SERVER_ERROR.getCode(), e.getMessage(), null);
        }
    }


}
