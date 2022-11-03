package travel.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import travel.util.helper.listener.BaseTimeEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class User extends BaseTimeEntity {

    public User() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    private String userName;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY
            , cascade = {CascadeType.ALL}
            , orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<Travel> travelList = new ArrayList<>();

    public void addTravel(Travel... travels) {
        Collections.addAll(this.travelList, travels);
    }

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY
            , cascade = {CascadeType.ALL}
            , orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<SearchHistory> searchHistoryList = new ArrayList<>();

    public void addHistory(SearchHistory... histories) {
        Collections.addAll(this.searchHistoryList, histories);
    }



}
