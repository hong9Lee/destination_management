package travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travel.domain.City;

public interface CityRepository extends JpaRepository<City, Long> {

    boolean existsByFullAddr(String fullAddr);

    City findByFullAddr(String fullAddr);

}
