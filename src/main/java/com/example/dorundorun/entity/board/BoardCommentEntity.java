package com.example.dorundorun.entity;

import com.example.dorundorun.dto.BoardCommentDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "boardComment")
public class BoardCommentEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardCommentId;

    @Column
    private String boardCommentContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardId")
    private BoardEntity boardEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "memberId")
    private MemberEntity memberEntity;

    public static BoardCommentEntity toSaveEntity(BoardCommentDTO commentDTO, BoardEntity boardEntity, MemberEntity memberEntity) {
        BoardCommentEntity boardCommentEntity = new BoardCommentEntity();
        boardCommentEntity.setMemberEntity(memberEntity);
        boardCommentEntity.setBoardCommentContent(commentDTO.getBoardCommentContent());
        boardCommentEntity.setBoardEntity(boardEntity);

        return boardCommentEntity;
    }
}
