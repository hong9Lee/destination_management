package travel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import travel.domain.dto.CityDto;
import travel.service.CityService;

@RestController
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @PostMapping("/city")
    public ResponseEntity city(@RequestBody CityDto cityDTO) {
        cityService.cityHandler(cityDTO);
        return null;
    }

    @GetMapping ("/city")
    public ResponseEntity city(@RequestParam(name = "id") long id) {
        return new ResponseEntity<>(cityService.findCity(id), HttpStatus.OK);
    }
}
