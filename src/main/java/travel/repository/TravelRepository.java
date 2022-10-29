package travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travel.domain.City;
import travel.domain.Travel;

public interface TravelRepository extends JpaRepository<Travel, Long> {

}
