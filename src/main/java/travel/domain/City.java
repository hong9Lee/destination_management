package travel.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import travel.util.helper.listener.BaseTimeEntity;
import travel.util.helper.listener.SearchListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(value = {SearchListener.class})
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class City extends BaseTimeEntity {

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
    @JsonManagedReference
    @Builder.Default
    private List<Travel> travelList = new ArrayList<>();


    public void addTravel(Travel... travels) {
        Collections.addAll(this.travelList, travels);
    }
}
