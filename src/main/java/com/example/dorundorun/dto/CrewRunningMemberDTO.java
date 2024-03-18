package com.example.dorundorun.dto;

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
}
