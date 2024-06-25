package com.example.dorundorun.service;

import com.example.dorundorun.dto.CrewBoardCommentDTO;
import com.example.dorundorun.entity.*;
import com.example.dorundorun.repository.crew.CrewRepository;
import com.example.dorundorun.repository.crewBoard.CrewBoardCommentRepository;
import com.example.dorundorun.repository.crewBoard.CrewBoardRepository;
import com.example.dorundorun.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CrewBoardCommentService {
    private final CrewBoardCommentRepository crewBoardCommentRepository;
    private final CrewBoardRepository crewBoardRepository;
    private final MemberRepository memberRepository;
    private final CrewRepository crewRepository;


    public List<CrewBoardCommentDTO> findAll(Long crewBoardId) {
        CrewBoardEntity crewBoardEntity = crewBoardRepository.findById(crewBoardId).get();
        List<CrewBoardCommentEntity> commentEntityList = crewBoardCommentRepository.findAllByCrewBoardEntityOrderByCrewBoardCommentIdDesc(crewBoardEntity);

        List<CrewBoardCommentDTO> commentDTOList = new ArrayList<>();
        for(CrewBoardCommentEntity commentEntity:commentEntityList){
            String userNickname = commentEntity.getMemberEntity().getMemberNickname();
            CrewBoardCommentDTO commentDTO = CrewBoardCommentDTO.toCrewBoardCommentDTO(commentEntity, crewBoardId, userNickname);
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }

    public Long save(CrewBoardCommentDTO commentDTO, String username) {
        MemberEntity memberEntity = memberRepository.findByUsername(username).get();
        Optional<CrewBoardEntity> byId = crewBoardRepository.findById(commentDTO.getCrewBoardId());
        if(byId.isPresent()){
            CrewBoardEntity crewBoardEntity = byId.get();
            CrewBoardCommentEntity commentEntity = CrewBoardCommentEntity.toSaveEntity(commentDTO, crewBoardEntity, memberEntity);
            return crewBoardCommentRepository.save(commentEntity).getCrewBoardCommentId();
        }else{
            return null;
        }
    }
}
