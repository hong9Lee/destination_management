package travel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import travel.domain.dto.req.city.AddCityDto;
import travel.domain.dto.req.city.DelCityDto;
import travel.domain.dto.req.city.ModCityDto;
import travel.domain.dto.res.CityResDto;
import travel.domain.dto.res.SingleResultDto;
import travel.service.CityService;

@RestController
@RequiredArgsConstructor
@RequestMapping("city")
public class CityController {

    private final CityService cityService;

    /** 도시 등록  */
    @PostMapping("/add")
    public ResponseEntity addCity(@RequestBody AddCityDto dto) {
        CityResDto result = cityService.add(dto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getCode()));
    }

    /** 도시 수정  */
    @PostMapping("/mod")
    public ResponseEntity modCity(@RequestBody ModCityDto dto) {
        CityResDto result = cityService.mod(dto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getCode()));
    }

    /** 도시 삭제 */
    @PostMapping("/del")
    public ResponseEntity delCity(@RequestBody DelCityDto dto) {
        CityResDto result = cityService.del(dto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getCode()));
    }

    /** 도시 단건 조회 */
    @GetMapping ("/single")
    public ResponseEntity getSingleCity(@RequestParam(name = "city_id") long cityId,
                                        @RequestParam(name = "user_id") long userId) {
        SingleResultDto result = cityService.getCitySingleResult(cityId, userId);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getCode()));
    }

    @GetMapping ("/city-list")
    public ResponseEntity getCityList(@RequestParam(name = "id") long userId) {
        SingleResultDto result = cityService.getCityList(userId);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getCode()));
    }
}
