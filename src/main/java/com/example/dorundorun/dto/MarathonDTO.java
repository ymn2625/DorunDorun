package com.example.dorundorun.dto;

import com.example.dorundorun.entity.MarathonEntity;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MarathonDTO {
    private Long marathonId;
    private String marathonName;
    private String marathonContent;
    private String marathonDate;

    public static MarathonDTO toMarathonDTO(MarathonEntity marathonEntity) {
        MarathonDTO marathonDTO = new MarathonDTO();
        marathonDTO.setMarathonId(marathonEntity.getMarathonId());
        marathonDTO.setMarathonName(marathonEntity.getMarathonName());
        marathonDTO.setMarathonContent(marathonEntity.getMarathonContent());
        marathonDTO.setMarathonDate(marathonEntity.getMarathonDate());

        return marathonDTO;
    }

}
