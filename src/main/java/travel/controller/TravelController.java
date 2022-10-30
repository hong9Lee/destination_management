package travel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import travel.domain.dto.req.AddTravelDto;
import travel.domain.dto.req.DelTravelDto;
import travel.domain.dto.req.ModTravelDto;
import travel.domain.dto.res.TravelResDto;
import travel.domain.dto.res.TravelSingleResultDto;
import travel.service.TravelService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("travel")
public class TravelController {

    private final TravelService travelService;

    @PostMapping("/add")
    public ResponseEntity addTravel(@Valid @RequestBody AddTravelDto dto) {
        TravelResDto result = travelService.add(dto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getCode()));
    }

    @PostMapping("/mod")
    public ResponseEntity modTravel(@Valid @RequestBody ModTravelDto dto) {
        TravelResDto result = travelService.mod(dto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getCode()));
    }

    @PostMapping("/del")
    public ResponseEntity delTravel(@Valid @RequestBody DelTravelDto dto) {
        TravelResDto result = travelService.del(dto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getCode()));
    }

    @GetMapping("/single")
    public ResponseEntity getSingleTravel(@RequestParam(name = "id") Long id) {
        TravelSingleResultDto result = travelService.getTravelSingleResult(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getCode()));
    }

}
