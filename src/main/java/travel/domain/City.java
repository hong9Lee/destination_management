package travel.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private String addr_1; // 도
    private String addr_2; // 시
    private String cityName;

    private String fullAddr;

    private String explanation;

    @OneToMany(mappedBy = "city"
            , fetch = FetchType.LAZY
            , cascade = {CascadeType.ALL}
            , orphanRemoval = true)
    private List<Travel> travelList = new ArrayList<>();

    public void addTravel(Travel... travels) {
        Collections.addAll(this.travelList, travels);
    }
}
