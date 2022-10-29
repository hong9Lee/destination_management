package travel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import travel.domain.City;
import travel.domain.dto.CityDto;
import travel.repository.CityRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CityService {

    private final CityRepository cityRepository;

    public void cityHandler(CityDto cityDTO) {
        String type = cityDTO.getType();
        if (type.equals("ADD")) {
            addCity(cityDTO);
        } else if (type.equals("MOD")) {
            modCityInfo(cityDTO);
        } else if (type.equals("DEL")) {
            deleteCity(cityDTO);
        }
    }

    public void addCity(CityDto cityDTO) {
        System.out.println(cityRepository.existsByCityName(cityDTO.getCityName()));

        if(!cityRepository.existsByCityName(cityDTO.getCityName())){
            City city = City.builder()
                    .cityName(cityDTO.getCityName())
                    .explanation(cityDTO.getExplanation())
                    .cityToken(UUID.randomUUID().toString())
                    .build();
            cityRepository.save(city);
        }

    }

    public void modCityInfo(CityDto cityDTO) {
        System.out.println(cityRepository.existsByCityName(cityDTO.getCityName()));


        if(cityRepository.existsByCityName(cityDTO.getCityName())){
            City findCity = cityRepository.findByCityName(cityDTO.getCityName());
            findCity.setCityName(cityDTO.getCityName());
            findCity.setExplanation(cityDTO.getExplanation());

            cityRepository.save(findCity);
        }
    }

    public void deleteCity(CityDto cityDTO) {
        cityRepository.deleteByCityToken(cityDTO.getCityToken());
    }


    public CityDto findCity(long id) {
        City getCity = cityRepository.getById(id);
        return CityDto.builder()
                .cityId(getCity.getId())
                .cityName(getCity.getCityName())
                .cityToken(getCity.getCityToken())
                .explanation(getCity.getExplanation())
                .build();

    }


}
