package travel.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import travel.util.helper.listener.BaseTimeEntity;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Travel extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_id")
    private long id;

    private String title; // 여행 제목

    private LocalDate startDate; // 여행 시작일
    private LocalDate endDate; // 여행 종료일

    @ManyToOne
    @JsonBackReference
    private City city;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private User user;


    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        if(this.endDate == null) this.endDate = startDate;
    }

}
