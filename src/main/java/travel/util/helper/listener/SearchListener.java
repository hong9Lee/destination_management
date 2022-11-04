package travel.util.helper.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import travel.domain.City;
import travel.domain.SearchHistory;
import travel.domain.User;
import travel.repository.CityRepository;
import travel.repository.SearchHistoryRepository;
import travel.repository.UserRepository;

import java.time.LocalDateTime;

@Component
public class SearchListener implements ApplicationListener<SearchEvent> {

    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityRepository cityRepository;

    public SearchListener(SearchHistoryRepository searchHistoryRepository) {
        this.searchHistoryRepository = searchHistoryRepository;
    }

    public SearchListener() {}

    @Override
    public void onApplicationEvent(SearchEvent event) {
        SearchHistory searchHistory = new SearchHistory();

        searchHistory.setSearchDate(LocalDateTime.now());

        User getUser = userRepository.getById(event.getUserId());
        City getCity = cityRepository.getById(event.getCityId());

        getUser.addHistory(searchHistory);
        searchHistory.setUser(getUser);
        searchHistory.setCity(getCity);

        userRepository.save(getUser);
        searchHistoryRepository.save(searchHistory);
    }
}
