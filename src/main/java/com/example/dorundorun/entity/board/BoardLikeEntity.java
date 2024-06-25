package com.example.dorundorun.entity;

import com.example.dorundorun.dto.BoardLikeDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "boardLike")
public class BoardLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardLikeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="memberId")
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="boardId")
    private BoardEntity boardEntity;

    @Column
    private Integer boardLike;

    @Column
    private Integer boardHate;

    public static BoardLikeEntity toBoardLikeEntity(BoardEntity boardEntity, MemberEntity memberEntity) {
        BoardLikeEntity boardLikeEntity = new BoardLikeEntity();
        boardLikeEntity.setBoardEntity(boardEntity);
        boardLikeEntity.setMemberEntity(memberEntity);

        return boardLikeEntity;
    }
}
