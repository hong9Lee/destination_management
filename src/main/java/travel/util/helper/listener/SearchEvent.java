package travel.util.helper.listener;

import org.springframework.context.ApplicationEvent;
import java.time.Clock;


public class SearchEvent extends ApplicationEvent {

    private Long userId;
    private Long cityId;

    public SearchEvent(Object source, Long userId, Long cityId) {
        super(source);
        this.userId = userId;
        this.cityId = cityId;
    }

    public SearchEvent(Object source, Clock clock, Long userId, Long cityId) {
        super(source, clock);
        this.userId = userId;
        this.cityId = cityId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCityId() {
        return cityId;
    }
}
