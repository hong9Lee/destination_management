package travel.util.helper;

import travel.domain.City;
import travel.domain.SearchHistory;
import travel.domain.Travel;
import travel.domain.User;
import travel.domain.dto.CityDto;
import travel.domain.dto.req.city.AddCityDto;
import travel.domain.dto.res.CityResultDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CityListUtils {
    /** 여행 시작일이 가까운 여행중인 도시 리스트 반환 (중복 허용) */
    public static ArrayList<City> getTravelingCityList(List<Travel> travelList) {
        ArrayList<Travel> travelingList = new ArrayList<>();

        for (int i = 0; i < travelList.size(); i++) {
            Travel travel = travelList.get(i);

            if(travel.getStartDate().isBefore(LocalDate.now())
                    && travel.getEndDate().isAfter(LocalDate.now())){
                travelingList.add(travel);
                travelList.remove(travel);
            }
        }

        travelingList.sort(Comparator.comparing(Travel::getStartDate));
        return getCityByTravel(travelingList);
    }

    /** 여행이 예정된 도시 : 정렬(여행 시작일이 가까운 것부터) 리스트 반환 */
    public static ArrayList<City> getFutureTravelCityList(List<Travel> travelList) {
        ArrayList<Travel> futureTravelList = new ArrayList<>();
        for (int i = 0; i < travelList.size(); i++) {
            Travel travel = travelList.get(i);

            if (travel.getStartDate().isAfter(LocalDate.now())) {
                futureTravelList.add(travel);
                travelList.remove(travel);
            }
        }

        futureTravelList.sort(Comparator.comparing(Travel::getStartDate));
        return Utils.getDupList(getCityByTravel(futureTravelList));
    }

    /** 하루 이내에 등록된 도시 : 정렬(가장 최근에 등록한 도시) 리스트 반환(중복 제거) */
    public static ArrayList<City> getRecentRegistrationCityList(List<Travel> travelList) {
        ArrayList<Travel> recentRegistrationCityList = new ArrayList<>();

        for (int i = 0; i < travelList.size(); i++) {
            Travel travel = travelList.get(i);

            if(Utils.calcRecentDate(-1, travel.getCreatedAt()))
                recentRegistrationCityList.add(travel);
            travelList.remove(travel);
        }

        recentRegistrationCityList.sort(Comparator.comparing(Travel::getCreatedAt));
        return Utils.getDupList(getCityByTravel(recentRegistrationCityList));
    }

    /** 최근 일주일 이내에 한 번 이상 조회된 도시 : 정렬(가장 최근 조회) 리스트 반환(중복 제거) */
    public static ArrayList<City> getSearchList(User getUser, List<Travel> travelList) {
        List<SearchHistory> searchHistoryList = getUser.getSearchHistoryList();

        ArrayList<City> searchList = new ArrayList<>();
        searchHistoryList.sort(Comparator.comparing(SearchHistory::getSearchDate));

        for (int i = 0; i < searchHistoryList.size(); i++) {
            SearchHistory history = searchHistoryList.get(i);

            if(Utils.calcRecentDate(-7, history.getSearchDate())) {
                City city = history.getCity();
                searchList.add(city);

                for (int j = 0; j < travelList.size(); j++) {
                    City getCity = travelList.get(j).getCity();
                    if(getCity.getId() == city.getId()){
                        travelList.remove(travelList.get(j));
                    }
                }
            }
        }
        return Utils.getDupList(searchList);
    }

    /** Travel List 에서 추출한 City 리스트 반환 */
    public static ArrayList<City> getCityByTravel(List<Travel> travelList) {

        ArrayList<City> cityList = new ArrayList<>();
        for (int i = 0; i < travelList.size(); i++) {
            cityList.add(travelList.get(i).getCity());
        }

        return cityList;
    }

    public static CityDto cityDtoMapper(City city) {
        return CityDto.builder()
                .cityId(city.getId())
                .addr_1(city.getAddr_1())
                .addr_2(city.getAddr_2())
                .cityName(city.getCityName())
                .fullAddr(city.getFullAddr())
                .explanation(city.getExplanation())
                .build();
    }

    public static City cityMapper(AddCityDto dto) {
        return City.builder()
                .addr_1(dto.getAddr_1())
                .addr_2(dto.getAddr_2())
                .cityName(dto.getCityName())
                .fullAddr(dto.getFullAddr())
                .explanation(dto.getExplanation())
                .build();
    }

    public static ArrayList<CityResultDto> cityResultMapper(ArrayList<CityResultDto> resDto, ArrayList<City> cityResult, String travelingFlag) {
        cityResult.forEach(item -> {
            resDto.add(CityResultDto.builder()
                    .id(item.getId())
                    .isTraveling(travelingFlag)
                    .addr_1(item.getAddr_1())
                    .addr_2(item.getAddr_2())
                    .cityName(item.getCityName())
                    .fullAddr(item.getFullAddr())
                    .explanation(item.getExplanation())
                    .build());
        });
        return resDto;
    }

    /** City List 결과값 DTO로 반환 */
    public static ArrayList<CityResultDto> setResultDto(ArrayList<City> travelingResult, ArrayList<City> dupList) {
        ArrayList<CityResultDto> resDto = new ArrayList<>();
        resDto = cityResultMapper(resDto, travelingResult, "Y");
        resDto = cityResultMapper(resDto, Utils.getDupList(dupList), "N");
        return resDto;
    }
}
