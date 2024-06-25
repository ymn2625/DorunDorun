package com.example.dorundorun.entity;

import com.example.dorundorun.dto.BoardCommentDTO;
import com.example.dorundorun.dto.CrewBoardCommentDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "crewBoardComment")
public class CrewBoardCommentEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long crewBoardCommentId;

    @Column
    private String crewBoardCommentContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crewBoardId")
    private CrewBoardEntity crewBoardEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "memberId")
    private MemberEntity memberEntity;

    public static CrewBoardCommentEntity toSaveEntity(CrewBoardCommentDTO commentDTO, CrewBoardEntity crewBoardEntity, MemberEntity memberEntity) {
        CrewBoardCommentEntity crewBoardCommentEntity = new CrewBoardCommentEntity();
        crewBoardCommentEntity.setMemberEntity(memberEntity);
        crewBoardCommentEntity.setCrewBoardCommentContent(commentDTO.getCrewBoardCommentContent());
        crewBoardCommentEntity.setCrewBoardEntity(crewBoardEntity);

        return crewBoardCommentEntity;
    }
}
