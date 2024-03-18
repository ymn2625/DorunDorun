package com.example.dorundorun.dto;

import com.example.dorundorun.entity.CrewMemberEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CrewMemberDTO {
    private Long crewMemberId;
    private String crewMemberAuth;
    private String memberNickname;
    private String memberId;
    private Long crewId;

    private LocalDateTime crewCreatedTime;
    private LocalDateTime crewUpdatedTime;

    public static CrewMemberDTO toCrewMemberDTO(CrewMemberEntity crewMemberEntity) {
        CrewMemberDTO crewMemberDTO = new CrewMemberDTO();
        crewMemberDTO.setCrewMemberId(crewMemberEntity.getCrewMemberId());
        crewMemberDTO.setCrewMemberAuth(crewMemberEntity.getRole());
        crewMemberDTO.setMemberId(crewMemberEntity.getMemberEntity().getUsername());
        crewMemberDTO.setMemberNickname(crewMemberEntity.getMemberEntity().getMemberNickname());
        crewMemberDTO.setCrewId(crewMemberEntity.getCrewEntity().getCrewId());

        return crewMemberDTO;
    }
}
