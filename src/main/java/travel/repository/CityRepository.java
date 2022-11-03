package travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import travel.domain.City;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {

    boolean existsByFullAddr(String fullAddr);

    City findByFullAddr(String fullAddr);

    @Query(value = "select c.* " +
            "from travel t right join city c on c.id = t.city_id " +
            "where t.start_date <= now() AND t.end_date >= now() AND t.user_id =:userId " +
            "order by t.start_date asc", nativeQuery = true)
    List<City> findTravelingByUserId(Long userId);


    @Query(value = "select DISTINCT t.start_date, c.* " +
            "from travel t right join city c on c.id = t.city_id " +
            "where t.start_date >= now() AND t.user_id =:userId " +
            "order by t.start_date asc", nativeQuery = true)
    List<City> findFutureTravelByUserId(Long userId);

}
