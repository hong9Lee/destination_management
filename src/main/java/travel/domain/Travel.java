package travel.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Travel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    private City city;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        if(this.endDate == null) this.endDate = startDate;
    }

}
