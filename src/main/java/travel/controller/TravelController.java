package travel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import travel.domain.dto.TravelDto;
import travel.domain.dto.res.TravelResDto;
import travel.service.TravelService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class TravelController {

    private final TravelService travelService;

    @PostMapping("/travel")
    public ResponseEntity travel(@Valid @RequestBody TravelDto travelDTO) {
        TravelResDto res = travelService.travelHandler(travelDTO);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/travel")
    public ResponseEntity travel(@RequestParam(name = "id") Long id) {
        return new ResponseEntity<>(travelService.getTravel(id), HttpStatus.OK);
    }

}
