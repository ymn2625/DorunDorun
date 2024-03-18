package com.example.dorundorun.service;

import com.example.dorundorun.dto.CrewDTO;
import com.example.dorundorun.dto.CrewMemberDTO;
import com.example.dorundorun.entity.*;
import com.example.dorundorun.repository.CrewFileRepository;
import com.example.dorundorun.repository.CrewMemberRepository;
import com.example.dorundorun.repository.CrewRepository;
import com.example.dorundorun.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CrewService {

    private final CrewRepository crewRepository;
    private final MemberRepository memberRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final CrewFileRepository crewFileRepository;

    //크루만들기
    public CrewDTO crewCreate(CrewDTO crewDTO, String username) throws IOException {

        MemberEntity memberEntity = memberRepository.findByUsername(username).get();

        if (crewDTO.getCrewFile().isEmpty()) {

            CrewEntity crewEntity = CrewEntity.toSaveCrewEntity(crewDTO);

            crewRepository.save(crewEntity);
        } else {

            CrewEntity crewEntity = CrewEntity.toFileCrewEntity(crewDTO);
            Long crewId = crewRepository.save(crewEntity).getCrewId();
            CrewEntity crew = crewRepository.findById(crewId).get();
            for (MultipartFile crewFile : crewDTO.getCrewFile()) {
                String originalFilename = crewFile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis() + "_" + username + "_" + originalFilename;
                String savePath = "C:/pg/springboot_img/" + storedFileName;
                crewFile.transferTo(new File(savePath));

                CrewFileEntity crewFileEntity = CrewFileEntity.toCrewFileEntity(crew, originalFilename, storedFileName);
                crewFileRepository.save(crewFileEntity);
            }
        }
        CrewEntity crewEntity = CrewEntity.toSaveCrewEntity(crewDTO);

        CrewEntity myCrewEntity = crewRepository.findByCrewName(crewEntity.getCrewName());

        CrewMemberEntity crewMemberEntity = new CrewMemberEntity();
        crewMemberEntity.setRole("CREW_ADMIN");
        crewMemberEntity.setCrewEntity(myCrewEntity);
        crewMemberEntity.setMemberEntity(memberEntity);

        crewMemberRepository.save(crewMemberEntity);

        return CrewDTO.toCrewDTO(myCrewEntity);
    }

    //CrewDTO crewId로 찾기
    public CrewDTO findById(Long id) {
        CrewEntity crewEntity = crewRepository.findById(id).get();

        return CrewDTO.toCrewDTO(crewEntity);
    }

    //CrewDTO로 Crew 인원수 확인
    public int countCrew(CrewDTO crewDTO) {
        return crewMemberRepository.countCrewMember(crewDTO.getCrewId());
    }

    //CrewDTOList 찾기
    public List<CrewDTO> findAll() {
        List<CrewEntity> crewEntityList = crewRepository.findAll();
        List<CrewDTO> crewDTOList = new ArrayList<>();

        for(CrewEntity crewEntity : crewEntityList){
            CrewDTO crewDTO = CrewDTO.toCrewDTO(crewEntity);
            int countCrewMember = crewMemberRepository.countCrewMember(crewDTO.getCrewId());
            crewDTO.setCrewMemberCount(countCrewMember);
            crewDTOList.add(crewDTO);
        }

        return crewDTOList;
    }

    //크루에 가입되어있는 멤버인지 찾기
    public Boolean findByCrewDTO(CrewDTO crewDTO, String username) {
        CrewEntity crewEntity = CrewEntity.toFindCrewEntity(crewDTO);
        MemberEntity memberEntity = memberRepository.findByUsername(username).get();

        Optional<CrewMemberEntity> crewMemberEntity = crewMemberRepository.findByCrewEntityAndMemberEntity(crewEntity, memberEntity);
        return crewMemberEntity.isPresent();
    }

    //크루 가입하기
    public void joinCrew(String username, long crewId) {

        MemberEntity memberEntity = memberRepository.findByUsername(username).get();
        CrewEntity crewEntity = crewRepository.findById(crewId).get();

        CrewMemberEntity crewMemberEntity = new CrewMemberEntity();
        crewMemberEntity.setMemberEntity(memberEntity);
        crewMemberEntity.setCrewEntity(crewEntity);
        crewMemberEntity.setRole("CREW_MEMBER");

        crewMemberRepository.save(crewMemberEntity);
    }

    //크루의 크루인원 리스트 찾기
    public List<CrewMemberDTO> crewMember(CrewDTO crewDTO) {
        CrewEntity crewEntity = CrewEntity.toFindCrewEntity(crewDTO);
        List<CrewMemberEntity> crewMemberEntityList = crewMemberRepository.findAllByCrewEntityOrderByRole(crewEntity);
        List<CrewMemberDTO> crewMemberDTOList = new ArrayList<>();
        for(CrewMemberEntity crewMemberEntity : crewMemberEntityList){
            CrewMemberDTO crewMemberDTO = CrewMemberDTO.toCrewMemberDTO(crewMemberEntity);
            crewMemberDTOList.add(crewMemberDTO);
        }

        return crewMemberDTOList;
    }

    //크루장인지 확인
    public Boolean findByCrewMemberAuth(CrewDTO crewDTO, String username) {
        CrewEntity crewEntity = CrewEntity.toFindCrewEntity(crewDTO);
        MemberEntity memberEntity = memberRepository.findByUsername(username).get();

        Optional<CrewMemberEntity> crewMemberEntity = crewMemberRepository.findByCrewEntityAndMemberEntity(crewEntity, memberEntity);
        return crewMemberEntity.map(entity -> entity.getRole().equals("CREW_ADMIN")).orElse(false);
    }

    //크루 관리자인지 확인
    public Boolean findCrewMember(CrewDTO crewDTO, String username) {
        CrewEntity crewEntity = CrewEntity.toFindCrewEntity(crewDTO);
        MemberEntity memberEntity = memberRepository.findByUsername(username).get();

        Optional<CrewMemberEntity> crewMemberEntity = crewMemberRepository.findByCrewEntityAndMemberEntity(crewEntity, memberEntity);
        return crewMemberEntity.map(entity -> entity.getRole().equals("CREW_MEMBER")).orElse(false);
    }

    //크루 삭제
    public void deleteById(Long id) {
        crewRepository.deleteById(id);
    }

    //크루 업데이트
    public void update(CrewDTO crewDTO) {
        CrewEntity crewEntity = CrewEntity.toFindCrewEntity(crewDTO);
        crewRepository.save(crewEntity);
    }

    //크루 매니저로 변경
    public CrewMemberDTO makeManager(Long id) {
        CrewMemberEntity crewMemberEntity = crewMemberRepository.findById(id).get();
        crewMemberEntity.setRole("CREW_MANAGER");
        crewMemberRepository.save(crewMemberEntity);
        CrewMemberDTO crewMemberDTO = CrewMemberDTO.toCrewMemberDTO(crewMemberEntity);
        return crewMemberDTO;
    }

    //크루멤버아이디로 크루멤버DTO찾기
    public CrewMemberDTO findCrewMemberDTO(Long id) {
        CrewMemberEntity crewMemberEntity = crewMemberRepository.findById(id).get();
        return CrewMemberDTO.toCrewMemberDTO(crewMemberEntity);
    }

    //크루 매니저 해임
    public CrewMemberDTO makeMember(Long id) {
        CrewMemberEntity crewMemberEntity = crewMemberRepository.findById(id).get();
        crewMemberEntity.setRole("CREW_MEMBER");
        crewMemberRepository.save(crewMemberEntity);
        CrewMemberDTO crewMemberDTO = CrewMemberDTO.toCrewMemberDTO(crewMemberEntity);
        return crewMemberDTO;
    }

    //크루 관리자 등록
    public CrewMemberDTO makeAdmin(Long id, CrewDTO crewDTO) {
        long crewId = crewDTO.getCrewId();
        CrewMemberEntity myCrewEntity = crewMemberRepository.findAdminByCrewId(crewId);
        myCrewEntity.setRole("CREW_MANAGER");
        crewMemberRepository.save(myCrewEntity);

        CrewMemberEntity crewMemberEntity = crewMemberRepository.findById(id).get();
        crewMemberEntity.setRole("CREW_ADMIN");
        crewMemberRepository.save(crewMemberEntity);
        CrewMemberDTO crewMemberDTO = CrewMemberDTO.toCrewMemberDTO(crewMemberEntity);
        return crewMemberDTO;
    }

    //크루원 추방
    public CrewMemberDTO deleteMember(Long id) {
        CrewMemberEntity crewMemberEntity = crewMemberRepository.findById(id).get();
        CrewMemberDTO crewMemberDTO = CrewMemberDTO.toCrewMemberDTO(crewMemberEntity);

        crewMemberRepository.deleteById(id);

        return crewMemberDTO;
    }

    //크루 탈퇴
    public void quitCrew(String username, Long id) {
        MemberEntity memberEntity = memberRepository.findByUsername(username).get();
        CrewEntity crewEntity = crewRepository.findById(id).get();

        Optional<CrewMemberEntity> byCrewEntityAndMemberEntity = crewMemberRepository.findByCrewEntityAndMemberEntity(crewEntity, memberEntity);
        if(byCrewEntityAndMemberEntity.isPresent()){
            Long crewMemberId = byCrewEntityAndMemberEntity.get().getCrewMemberId();
            crewMemberRepository.deleteById(crewMemberId);
        }
    }

    //크루 매니저 찾기
    public Boolean findCrewManager(CrewDTO crewDTO, String username) {
        CrewEntity crewEntity = CrewEntity.toFindCrewEntity(crewDTO);
        MemberEntity memberEntity = memberRepository.findByUsername(username).get();

        Optional<CrewMemberEntity> crewMemberEntity = crewMemberRepository.findByCrewEntityAndMemberEntity(crewEntity, memberEntity);
        return crewMemberEntity.map(entity -> entity.getRole().equals("CREW_MANAGER") || entity.getRole().equals("CREW_ADMIN")).orElse(false);
    }

    public CrewMemberDTO findCrewMemberDTOByUserNameAndCrewId(String username, Long crewId) {
        Optional<CrewEntity> byCrewId = crewRepository.findById(crewId);
        Optional<MemberEntity> byUsername = memberRepository.findByUsername(username);
        if(byCrewId.isPresent() && byUsername.isPresent()){
            CrewEntity crewEntity = byCrewId.get();
            MemberEntity memberEntity = byUsername.get();
            Optional<CrewMemberEntity> byCrewEntityAndMemberEntity = crewMemberRepository.findByCrewEntityAndMemberEntity(crewEntity, memberEntity);

            return byCrewEntityAndMemberEntity.map(CrewMemberDTO::toCrewMemberDTO).orElse(null);
        }else{
            return null;
        }
    }
}
