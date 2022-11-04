package travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travel.domain.SearchHistory;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> { }
