package com.example.dorundorun.entity;

import com.example.dorundorun.dto.MarathonDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "marathon")
public class MarathonEntity {

    @Id
    private Long marathonId;

    @Column
    private String marathonName;

    @Column
    private String marathonContent;

    @Column
    private String marathonDate;

    public static MarathonEntity toMarathonEntity(MarathonDTO marathonDTO) {
        MarathonEntity marathonEntity = new MarathonEntity();
        marathonEntity.setMarathonId(marathonDTO.getMarathonId());
        marathonEntity.setMarathonName(marathonDTO.getMarathonName());
        marathonEntity.setMarathonContent(marathonDTO.getMarathonContent());
        marathonEntity.setMarathonDate(marathonDTO.getMarathonDate());

        return marathonEntity;
    }

}
