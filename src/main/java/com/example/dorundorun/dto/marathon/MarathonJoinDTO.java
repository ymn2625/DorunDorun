package com.example.dorundorun.dto;

import com.example.dorundorun.entity.marathon.MarathonJoinEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MarathonJoinDTO {

    private Long marathonJoinId;
    private Long marathonId;
    private Long marathonCourseId;
    private String memberId;
    private boolean cancel;
    private String reward;


    public static MarathonJoinDTO toMarathonJoinDTO(MarathonJoinEntity marathonJoinEntity) {

        MarathonJoinDTO marathonJoinDTO = new MarathonJoinDTO();
        marathonJoinDTO.setMarathonJoinId(marathonJoinEntity.getMarathonJoinId());
        marathonJoinDTO.setMarathonId(marathonJoinEntity.getMarathonEntity().getMarathonId());
        marathonJoinDTO.setMarathonCourseId(marathonJoinEntity.getMarathonCourseEntity().getMarathonCourseId());
        marathonJoinDTO.setMemberId(marathonJoinEntity.getMemberEntity().getUsername());
        marathonJoinDTO.setCancel(marathonJoinEntity.isCancel());
        marathonJoinDTO.setReward(marathonJoinEntity.getReward());
        return marathonJoinDTO;
    }

}
