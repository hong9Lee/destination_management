package travel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import travel.domain.dto.req.travel.AddTravelDto;
import travel.domain.dto.req.travel.DelTravelDto;
import travel.domain.dto.req.travel.ModTravelDto;
import travel.domain.dto.res.TravelResDto;
import travel.domain.dto.res.SingleResultDto;
import travel.service.TravelService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("travel")
public class TravelController {

    private final TravelService travelService;

    /** 여행 등록 */
    @PostMapping("/add")
    public ResponseEntity addTravel(@Valid @RequestBody AddTravelDto dto) {
        TravelResDto result = travelService.add(dto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getCode()));
    }

    /** 여행 수정 */
    @PostMapping("/mod")
    public ResponseEntity modTravel(@Valid @RequestBody ModTravelDto dto) {
        TravelResDto result = travelService.mod(dto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getCode()));
    }

    /** 여행 삭제 */
    @PostMapping("/del")
    public ResponseEntity delTravel(@Valid @RequestBody DelTravelDto dto) {
        TravelResDto result = travelService.del(dto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getCode()));
    }

    /** 여행 단일 조회 */
    @GetMapping("/single")
    public ResponseEntity getSingleTravel(@RequestParam(name = "id") Long id) {
        SingleResultDto result = travelService.getTravelSingleResult(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getCode()));
    }

}
