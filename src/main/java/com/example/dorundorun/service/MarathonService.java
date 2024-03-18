package com.example.dorundorun.service;

import com.example.dorundorun.dto.MarathonCourseDTO;
import com.example.dorundorun.dto.MarathonDTO;
import com.example.dorundorun.dto.MarathonJoinDTO;
import com.example.dorundorun.dto.MemberDTO;
import com.example.dorundorun.entity.MarathonCourseEntity;
import com.example.dorundorun.entity.MarathonEntity;
import com.example.dorundorun.entity.MarathonJoinEntity;
import com.example.dorundorun.entity.MemberEntity;
import com.example.dorundorun.repository.MarathonCourseRepository;
import com.example.dorundorun.repository.MarathonJoinRepository;
import com.example.dorundorun.repository.MarathonRepository;
import com.example.dorundorun.repository.MemberRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.Console;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import static com.example.dorundorun.dto.MarathonDTO.toMarathonDTO;
import static com.example.dorundorun.dto.MemberDTO.toMemberDTO;
import static com.example.dorundorun.entity.MemberEntity.toMemberEntity;

@Service
public class MarathonService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MarathonRepository marathonRepository;
    @Autowired
    private MarathonJoinRepository marathonJoinRepository; // 추가
    @Autowired
    private MarathonCourseRepository marathonCourseRepository;
    private static final Logger logger = LoggerFactory.getLogger(MarathonService.class);


    private final ModelMapper modelMapper;

    public MarathonService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public List<MarathonCourseDTO> getAllMarathonCourses() {
        List<MarathonCourseEntity> marathonCourseEntities = marathonCourseRepository.findAll();
        return marathonCourseEntities.stream()
                .map(MarathonCourseDTO::toMarathonCourseDTO)
                .collect(Collectors.toList());
    }

    private MarathonDTO toMarathonDTO(MarathonEntity marathonEntity) {
        MarathonDTO marathonDTO = new MarathonDTO();
        // MarathonEntity의 정보를 MarathonDTO로 변환하여 설정
        marathonDTO.setMarathonId(marathonEntity.getMarathonId());
        marathonDTO.setMarathonName(marathonEntity.getMarathonName());
        marathonDTO.setMarathonContent(marathonEntity.getMarathonContent());
        marathonDTO.setMarathonDate(marathonEntity.getMarathonDate());
        // 다른 속성도 필요에 따라 변환하여 설정
        return marathonDTO;
    }

    // 마라톤 id로 데이터 불러오기~
    public MarathonDTO getMarathonById(Long marathonId) {

        Optional<MarathonEntity> marathonOptional = marathonRepository.findById(marathonId);
        if (marathonOptional.isPresent()) {
            MarathonEntity marathonEntity = marathonOptional.get();
            // Marathon 엔티티를 MarathonDTO로 변환하여 반환
            return toMarathonDTO(marathonEntity);
        } else {
            throw new IllegalArgumentException("Marathon with ID " + marathonId + " not found");
        }
    }

    // 마라톤코스아이디로 데이터 불러오기
    public MarathonCourseDTO getMarathonCourseById(Long marathonCourseId) {
        Optional<MarathonCourseEntity> marathonCourse = marathonCourseRepository.findById(marathonCourseId);
        MarathonCourseEntity marathonCourseEntity = marathonCourse.get();
        return MarathonCourseDTO.toMarathonCourseDTO(marathonCourseEntity);
    }

    // 마라톤 조인아이디로 데이터 불러오기
    public MarathonJoinDTO getMarathonJoinById(Long marathonJoinId) {
        Optional<MarathonJoinEntity> marathonJoin = marathonJoinRepository.findById(marathonJoinId);
        MarathonJoinEntity marathonJoinEntity = marathonJoin.get();
        return MarathonJoinDTO.toMarathonJoinDTO(marathonJoinEntity);
    }
    // 마라톤조인아이디로 cancel값 바꾸기
    public MarathonJoinDTO marathonJoinCancelById(Long marathonJoinId) {
        MarathonJoinEntity marathonJoinEntity = marathonJoinRepository.findById(marathonJoinId)
                .orElseThrow(()-> new EntityNotFoundException("마라톤조인아이디못찾음?"+marathonJoinId));
        // cancel값 변경
        marathonJoinEntity.setCancel(true);
        // 변경값 저장
        marathonJoinRepository.save(marathonJoinEntity);
        // 변경된 값을 DTO로 변환해서 반환
        return MarathonJoinDTO.toMarathonJoinDTO(marathonJoinEntity);
    }

    // 현재 로그인한 회원의 정보를 반환하는 메서드
    public MemberDTO getCurrentMember() {
        // Spring Security를 사용하여 현재 로그인한 회원의 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 정보가 null이 아니고, Principal이 UserDetails를 구현하는 경우에만 현재 회원의 정보를 반환
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // 현재 로그인한 회원의 아이디를 사용하여 회원 정보를 가져옴
            String username = userDetails.getUsername();
            Optional<MemberEntity> memberEntityOptional = memberRepository.findByUsername(username);
            if (memberEntityOptional.isPresent()) {
                MemberEntity memberEntity = memberEntityOptional.get();
                return modelMapper.map(memberEntity, MemberDTO.class);
            }
        }
        return null; // 현재 회원이 없는 경우 null 반환
    }

    // 마라톤조인 테이블 만들기
    public void saveMarathonJoin(Long marathonId, Long marathonCourseId, String username, String reward) {

        logger.info("Attempting to save marathon join with marathonId: {}, marathonCourseId: {}, username: {}, reward: {}", marathonId, marathonCourseId, username, reward);

        // 현재 로그인한 회원의 정보를 가져옴
        MemberEntity memberEntity = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다: " + username));

        logger.info("Found member: {}", memberEntity);

        // MarathonJoinDTO 생성
        MarathonJoinDTO marathonJoinDTO = new MarathonJoinDTO();
        marathonJoinDTO.setMarathonId(marathonId);
        marathonJoinDTO.setMarathonCourseId(marathonCourseId);
        marathonJoinDTO.setMemberId(username);
        marathonJoinDTO.setReward(reward);

        // DTO를 Entity로 변환하여 저장
        MarathonJoinEntity marathonJoinEntity = MarathonJoinEntity.toMarathonJoinEntity(marathonJoinDTO,memberRepository);
        marathonJoinRepository.save(marathonJoinEntity);

        logger.info("Marathon join saved successfully");
    }

    // 마라톤조인 찾기
    public Long getMarathonJoinIdByMemberId(Long memberId) {
        // id를 기반으로 해당 회원이 참가한 마라톤의 JoinId를 찾습니다.
        MarathonJoinEntity marathonJoin = marathonJoinRepository.findByMemberEntityId(memberId);

        if (marathonJoin != null) {
            return marathonJoin.getMarathonJoinId();
        } else {
            return null;
        }
    }

    // 마라톤 조인 데이터 삭제 메서드(멤버로)
    public void deleteMarathonJoin(Long memberId) {
        // id를 기반으로 해당 회원이 참가한 마라톤의 JoinId를 찾습니다.
        MarathonJoinEntity marathonJoin = marathonJoinRepository.findByMemberEntityId(memberId);
        Long marathonJoinId = marathonJoin.getMarathonJoinId();
        // 여기서 marathonJoinId를 사용하여 해당 엔터티를 삭제합니다.
        marathonJoinRepository.deleteById(marathonJoinId);
    }

    // 마라톤 조인 데이터 삭제 (조인 아이디로)
    public void deleteCancelMarathon(Long marathonJoinId) {
        marathonJoinRepository.deleteById(marathonJoinId);
    }



}