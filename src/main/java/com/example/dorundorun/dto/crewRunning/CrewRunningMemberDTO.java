package com.example.dorundorun.dto;

import com.example.dorundorun.entity.CrewRunningMemberEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CrewRunningMemberDTO {

    private Long crewRunningMemberId;
    private String crewMemberId;
    private Long crewRunningId;
    private String memberNickname;
    private String memberId;
    private String crewRunningRole;

    public static CrewRunningMemberDTO toCrewRunningMemberDTO(CrewRunningMemberEntity crewRunningMemberEntity) {
        CrewRunningMemberDTO crewRunningMemberDTO = new CrewRunningMemberDTO();
        crewRunningMemberDTO.setCrewRunningMemberId(crewRunningMemberEntity.getCrewRunningMemberId());
        crewRunningMemberDTO.setMemberId(crewRunningMemberEntity.getMemberEntity().getUsername());
        crewRunningMemberDTO.setCrewRunningId(crewRunningMemberEntity.getCrewRunningEntity().getCrewRunningId());
        crewRunningMemberDTO.setMemberNickname(crewRunningMemberEntity.getMemberEntity().getMemberNickname());
        crewRunningMemberDTO.setCrewRunningRole(crewRunningMemberEntity.getCrewRunningRole());
        return crewRunningMemberDTO;
    }
}
