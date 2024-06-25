package com.example.dorundorun.entity;

import com.example.dorundorun.dto.MarathonCourseDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "marathonCourse")
public class MarathonCourseEntity {

    @Id
    private Long marathonCourseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marathonId")
    private MarathonEntity marathonEntity;

    @Column
    private String course;

    @Column
    private String price;

    

    public static MarathonCourseEntity toMarathonCourseEntity (MarathonCourseDTO marathonCourseDTO) {

        MarathonCourseEntity marathonCourseEntity = new MarathonCourseEntity();
        marathonCourseEntity.setMarathonCourseId(marathonCourseDTO.getMarathonCourseId());

        MarathonEntity marathonEntity = new MarathonEntity();
        marathonEntity.setMarathonId(marathonCourseDTO.getMarathonId());
        marathonCourseEntity.setMarathonEntity(marathonEntity);

        marathonCourseEntity.setCourse(marathonCourseDTO.getCourse());
        marathonCourseEntity.setPrice(marathonCourseDTO.getPrice());
        return marathonCourseEntity;
    }




}
