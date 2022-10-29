package travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travel.domain.City;

public interface CityRepository extends JpaRepository<City, Long> {
    boolean existsByCityName(String cityName);

    boolean existsByCityToken(String cityToken);
    City findByCityName(String cityName);

    Integer deleteByCityToken(String cityToken);
}
