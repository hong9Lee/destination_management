package travel.util.helper;

import travel.domain.City;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Utils {

    /** 변수 date가 현재 시간과 num 만큼의 차이를 boolean 값으로 반환하는 메서드 */
    public static boolean calcRecentDate(long num, LocalDateTime date) {
        LocalDateTime now = LocalDateTime.now();

        long between = ChronoUnit.DAYS.between(now.withHour(0).withMinute(0).withSecond(0), date.withHour(0).withMinute(0).withSecond(0));
        if(between >= num) return true;
        return false;
    }

    /** City List 중복 제거 */
    public static ArrayList<City> getDupList(ArrayList<City> list) {
        ArrayList<City> dupCityList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if(!dupCityList.contains(list.get(i))) dupCityList.add(list.get(i));
        }

        return dupCityList;
    }
}
