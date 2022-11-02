package travel.util.helper.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import travel.domain.SearchHistory;
import travel.repository.SearchHistoryRepository;

import java.time.LocalDateTime;

@Component
public class SearchListener implements ApplicationListener<SearchEvent> {

    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    public SearchListener(SearchHistoryRepository searchHistoryRepository) {
        this.searchHistoryRepository = searchHistoryRepository;
    }

    public SearchListener() {}

    @Override
    public void onApplicationEvent(SearchEvent event) {
        SearchHistory searchHistory = new SearchHistory();
        searchHistory.setUserId(event.getUserId());
        searchHistory.setCityId(event.getCityId());
        searchHistory.setSearchDate(LocalDateTime.now());

        searchHistoryRepository.save(searchHistory);
    }
}
