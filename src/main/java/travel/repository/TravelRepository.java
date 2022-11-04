package travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travel.domain.Travel;

public interface TravelRepository extends JpaRepository<Travel, Long> {
    Travel findByTitleAndUserId(String title, Long userId);
    boolean existsById(Long travelId);
}
