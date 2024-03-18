package com.example.dorundorun.dto;

import com.example.dorundorun.entity.RunningMemberEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RunningMemberDTO {

    private Long runningMemberId;
    private String memberId;
    private Long runningId;
    private String memberNickname;
    private String runningRole;

    public static RunningMemberDTO toRunningMemberDTO(RunningMemberEntity runningMemberEntity) {
        RunningMemberDTO runningMemberDTO = new RunningMemberDTO();
        runningMemberDTO.setRunningMemberId(runningMemberEntity.getRunningMemberId());
        runningMemberDTO.setMemberId(runningMemberEntity.getMemberEntity().getUsername());
        runningMemberDTO.setRunningId(runningMemberEntity.getRunningEntity().getRunningId());
        runningMemberDTO.setMemberNickname(runningMemberEntity.getMemberEntity().getMemberNickname());
        runningMemberDTO.setRunningRole(runningMemberEntity.getRunningRole());
        return runningMemberDTO;
    }
}
