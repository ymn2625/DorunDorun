package com.example.dorundorun.dto;

import com.example.dorundorun.entity.marathon.MarathonCourseEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MarathonCourseDTO {
    private Long marathonCourseId;
    private Long marathonId;
    private String course;
    private String price;

    public static MarathonCourseDTO toMarathonCourseDTO (MarathonCourseEntity marathonCourseEntity) {
        MarathonCourseDTO marathonCourseDTO = new MarathonCourseDTO();
        marathonCourseDTO.setMarathonCourseId(marathonCourseEntity.getMarathonCourseId());
        marathonCourseDTO.setMarathonId(marathonCourseEntity.getMarathonEntity().getMarathonId());
        marathonCourseDTO.setCourse(marathonCourseEntity.getCourse());
        marathonCourseDTO.setPrice(marathonCourseEntity.getPrice());
        return marathonCourseDTO;
    }

}
