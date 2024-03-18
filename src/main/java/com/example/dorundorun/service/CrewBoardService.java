package com.example.dorundorun.service;

import com.example.dorundorun.dto.*;
import com.example.dorundorun.entity.*;
import com.example.dorundorun.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CrewBoardService {

    private final CrewBoardRepository crewBoardRepository;
    private final MemberRepository memberRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final CrewBoardFileRepository crewBoardFileRepository;
    private final CrewBoardLikeRepository crewBoardLikeRepository;


    public void save(CrewBoardDTO crewBoardDTO, String username) throws IOException {

        //파일첨부 여부에 따른 로직 분리할 것
        if (crewBoardDTO.getCrewBoardFile().isEmpty()) {

            MemberEntity memberEntity = memberRepository.findByUsername(username).get();
            CrewBoardEntity crewBoardEntity = CrewBoardEntity.toCrewBoardEntity(crewBoardDTO, memberEntity);

            crewBoardRepository.save(crewBoardEntity);
        } else {
            MemberEntity memberEntity = memberRepository.findByUsername(username).get();
            CrewBoardEntity crewBoardEntity = CrewBoardEntity.toFileCrewBoardEntity(crewBoardDTO, memberEntity);
            Long crewBoardId = crewBoardRepository.save(crewBoardEntity).getCrewBoardId();
            CrewBoardEntity crewBoard = crewBoardRepository.findById(crewBoardId).get();
            for (MultipartFile crewBoardFile : crewBoardDTO.getCrewBoardFile()) {
                String crewBoardOriginalFilename = crewBoardFile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis() + "_" + username + "_" + crewBoardOriginalFilename;
                String savePath = "C:/pg/springboot_img/" + storedFileName;
                crewBoardFile.transferTo(new File(savePath));

                CrewBoardFileEntity crewBoardFileEntity = CrewBoardFileEntity.tocrewBoardFileEntity(crewBoard, crewBoardOriginalFilename, storedFileName);
                crewBoardFileRepository.save(crewBoardFileEntity);
            }
        }
    }

    @Transactional
    public Page<CrewBoardDTO> paging(Pageable pageable, String keyword, String condition, String category) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 10;
        Page<CrewBoardEntity> crewBoardEntities = null;
        System.out.println(category);
        if (category == null) {

            if (keyword == null || keyword.equals("")) {
                System.out.println("1");
                crewBoardEntities = crewBoardRepository.findAll(
                        PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "crewBoardId"))
                );
                System.out.println("2");
            } else if (condition.equals("boardTitle")) {
                System.out.println("3");
                crewBoardEntities = crewBoardRepository.findByCrewBoardTitleContaining(keyword,
                        PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "crewBoardId"))
                );
                System.out.println("4");
            } else if (condition.equals("memberNickname")) {
                //memberNickname을 포함한 멤버를 다 찾는다.
                System.out.println("5");
                List<MemberEntity> byMemberNicknameContaining = memberRepository.findByMemberNicknameContaining(keyword);
                crewBoardEntities = crewBoardRepository.findByMemberEntityIn(byMemberNicknameContaining,
                        PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "crewBoardId"))
                );
                System.out.println("6");
            }
        } else {
            System.out.println(category+condition+keyword);
            if (keyword == null || keyword.equals("")) {
                System.out.println("7");
                crewBoardEntities = crewBoardRepository.findByCrewBoardCategory(category,
                        PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "crewBoardId"))
                );
                System.out.println("8");
            } else if (condition.equals("crewBoardTitle")) {
                System.out.println("9");
                crewBoardEntities = crewBoardRepository.findByCrewBoardCategoryAndCrewBoardTitleContaining(category, keyword,
                        PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "crewBoardId"))
                );
                System.out.println("10");
                System.out.println(crewBoardEntities + "222222");
            } else if (condition.equals("memberNickname")) {
                //memberNickname을 포함한 멤버를 다 찾는다.
                System.out.println("11");
                List<MemberEntity> byMemberNicknameContaining = memberRepository.findByMemberNicknameContaining(keyword);
                crewBoardEntities = crewBoardRepository.findByCrewBoardCategoryAndMemberEntityIn(category, byMemberNicknameContaining,
                        PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "crewBoardId"))
                );
                System.out.println("12");
                System.out.println("crewBoardEntities = " + crewBoardEntities);
            }
        }

        Page<CrewBoardDTO> crwBoardDTOS = crewBoardEntities.map(crewBoard -> new CrewBoardDTO(crewBoard.getCrewBoardId(), crewBoard.getMemberEntity().getMemberNickname(), crewBoard.getCrewBoardTitle(), crewBoard.getCrewBoardCategory(), crewBoard.getCrewBoardHits(), crewBoard.getCreatedTime()));

        return crwBoardDTOS;
    }
    @Transactional
    public void updateHits(Long crewBoardId) {
        crewBoardRepository.updateHits(crewBoardId);
    }

    @Transactional
    public CrewBoardDTO findById(Long crewBoardId) {
        Optional<CrewBoardEntity> byId = crewBoardRepository.findById(crewBoardId);
        String username = byId.get().getMemberEntity().getUsername();
        if (byId.isPresent()) {
            CrewBoardEntity crewBoard = byId.get();
            CrewBoardDTO crewBoardDTO = CrewBoardDTO.toCrewBoardDTO(crewBoard, username);
            return crewBoardDTO;
        } else {
            return null;
        }
    }

    public CrewBoardLikeDTO findByCrewBoardId(MemberDTO memberDTO, Long crewBoardId) {
        CrewBoardEntity crewBoardEntity = crewBoardRepository.findById(crewBoardId).get();
//        MemberEntity memberEntity = crewBoardEntity.getMemberEntity();

        MemberEntity memberEntity = MemberEntity.toMemberUpdateMustBe(memberDTO);
        int countLike = crewBoardLikeRepository.countCrewBoardLike(crewBoardId);
        int countHate = crewBoardLikeRepository.countCrewBoardHate(crewBoardId);
        CrewBoardLikeEntity crewBoardLikeEntity = crewBoardLikeRepository.findByCrewBoardEntityAndMemberEntity(crewBoardEntity, memberEntity);

        CrewBoardLikeDTO crewBoardLikeDTO = new CrewBoardLikeDTO();
        if (crewBoardLikeEntity != null) {
            crewBoardLikeDTO = CrewBoardLikeDTO.toCrewBoardLikeDTO(crewBoardLikeEntity);
            crewBoardLikeDTO.setCountCrewBoardLike(countLike);
            crewBoardLikeDTO.setCountCrewBoardHate(countHate);
        } else {
            crewBoardLikeDTO.setCountCrewBoardHate(countLike);
            crewBoardLikeDTO.setCountCrewBoardLike(countHate);
        }
        return crewBoardLikeDTO;
    }

    public CrewBoardDTO update(CrewBoardDTO crewBoardDTO, String username) {
        MemberEntity memberEntity = memberRepository.findByUsername(username).get();
        crewBoardRepository.save(CrewBoardEntity.toUpdateCrewBoardEntity(crewBoardDTO, memberEntity));
        return findById(crewBoardDTO.getCrewBoardId());
    }

    @Transactional
    public Boolean like(CrewBoardLikeDTO crewBoardLikeDTO) {
        //부모엔티티 조회
        Optional<CrewBoardEntity> optionalCrewBoardEntity = crewBoardRepository.findById(crewBoardLikeDTO.getCrewBoardId());
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByUsername(crewBoardLikeDTO.getMemberId());
        if (optionalCrewBoardEntity.isPresent() && optionalMemberEntity.isPresent()) {
            CrewBoardEntity crewBoardEntity = optionalCrewBoardEntity.get();
            MemberEntity memberEntity = optionalMemberEntity.get();
            CrewBoardLikeEntity crewBoardLike = CrewBoardLikeEntity.toCrewBoardLikeEntity(crewBoardEntity, memberEntity);
            CrewBoardLikeEntity crewBoardLikeEntity = crewBoardLikeRepository.findByCrewBoardEntityAndMemberEntity(crewBoardEntity, memberEntity);
            if (crewBoardLikeEntity != null) {
                if (crewBoardLikeEntity.getCrewBoardLike() == 1) {
                    crewBoardLikeRepository.deleteById(crewBoardLikeEntity.getCrewBoardLikeId());
                } else {
                    if (crewBoardLikeEntity.getCrewBoardHate() == 1) {
                        crewBoardLikeEntity.setCrewBoardHate(0);
                        crewBoardLikeEntity.setCrewBoardLike(1);
                        crewBoardLikeRepository.save(crewBoardLikeEntity);
                    } else {
                        crewBoardLike.setCrewBoardLike(1);
                        crewBoardLike.setCrewBoardHate(0);
                        crewBoardLikeRepository.save(crewBoardLike);
                    }
                }
            } else {
                crewBoardLike.setCrewBoardHate(0);
                crewBoardLike.setCrewBoardLike(1);
                crewBoardLikeRepository.save(crewBoardLike);
            }
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public Boolean hate(CrewBoardLikeDTO crewBoardLikeDTO) {
        //부모엔티티 조회
        Optional<CrewBoardEntity> optionalCrewBoardEntity = crewBoardRepository.findById(crewBoardLikeDTO.getCrewBoardId());
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByUsername(crewBoardLikeDTO.getMemberId());
        if (optionalCrewBoardEntity.isPresent() && optionalMemberEntity.isPresent()) {
            CrewBoardEntity crewBoardEntity = optionalCrewBoardEntity.get();
            MemberEntity memberEntity = optionalMemberEntity.get();
            CrewBoardLikeEntity crewBoardLike = CrewBoardLikeEntity.toCrewBoardLikeEntity(crewBoardEntity, memberEntity);
            CrewBoardLikeEntity crewBoardLikeEntity = crewBoardLikeRepository.findByCrewBoardEntityAndMemberEntity(crewBoardEntity, memberEntity);
            if (crewBoardLikeEntity != null) {
                if (crewBoardLikeEntity.getCrewBoardHate() == 1) {
                    crewBoardLikeRepository.deleteById(crewBoardLikeEntity.getCrewBoardLikeId());
                } else {
                    if (crewBoardLikeEntity.getCrewBoardLike() == 1) {
                        crewBoardLikeEntity.setCrewBoardLike(0);
                        crewBoardLikeEntity.setCrewBoardHate(1);
                        crewBoardLikeRepository.save(crewBoardLikeEntity);
                    } else {
                        crewBoardLike.setCrewBoardHate(1);
                        crewBoardLike.setCrewBoardLike(0);
                        crewBoardLikeRepository.save(crewBoardLike);
                    }
                }
            } else {
                crewBoardLike.setCrewBoardLike(0);
                crewBoardLike.setCrewBoardHate(1);
                crewBoardLikeRepository.save(crewBoardLike);
            }
            return true;
        } else {
            return false;
        }
    }

    public void delete(Long crewBoardId) {
        crewBoardRepository.deleteById(crewBoardId);
    }
}
