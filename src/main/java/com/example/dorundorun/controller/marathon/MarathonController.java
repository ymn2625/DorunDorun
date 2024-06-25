package com.example.dorundorun.controller;

import com.example.dorundorun.dto.marathon.MarathonCourseDTO;
import com.example.dorundorun.dto.marathon.MarathonDTO;
import com.example.dorundorun.dto.member.MemberDTO;
import com.example.dorundorun.entity.member.MemberEntity;
import com.example.dorundorun.repository.member.MemberRepository;
import com.example.dorundorun.service.marathon.MarathonService;
import com.example.dorundorun.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/marathon")
public class MarathonController {

    private final MarathonService marathonService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    // 데이터 표시하기~
    @GetMapping("/intro")
    public String showMarathonDetails(@RequestParam("marathonId") Long marathonId, Model model) {

        // marathonId를 모델에 추가
        model.addAttribute("marathonId", marathonId);

        // 코스 정보 가져오기
        List<MarathonCourseDTO> marathonCourses = marathonService.getAllMarathonCourses();
        model.addAttribute("marathonCourses", marathonCourses);

        // 마라톤 이름 가져오기
        MarathonDTO marathon = marathonService.getMarathonById(marathonId);
        model.addAttribute("marathonName", marathon.getMarathonName());
        model.addAttribute("marathonContent", marathon.getMarathonContent());
        model.addAttribute("marathonDate", marathon.getMarathonDate());

        // 마라톤 상세 페이지로 이동
        return "marathon/event";
    }


    // 데이터 넘기기~
    @PostMapping("/joinForm")
    public String processJoinForm(@RequestParam("marathonId") Long marathonId,
                                  @RequestParam("marathonCourseId") Long marathonCourseId,
                                  @RequestParam("reward") String reward,
                                  Model model) {
        // 데이터를 저장합니다.
        model.addAttribute("marathonId", marathonId);
        model.addAttribute("marathonCourseId", marathonCourseId);
        model.addAttribute("reward", reward);

        // 마라톤 이름 넘겨서 가져오기
        MarathonDTO marathon = marathonService.getMarathonById(marathonId);
        model.addAttribute("marathonName", marathon.getMarathonName());

        // 코스 데이터 넘겨서 가져오기
        MarathonCourseDTO marathonCourse = marathonService.getMarathonCourseById(marathonCourseId);
        model.addAttribute("course", marathonCourse.getCourse());
        model.addAttribute("price", marathonCourse.getPrice());


        return "marathon/eventForm";
    }

    // 데이터 가져오기
    @GetMapping("/currentUser")
    public ResponseEntity<MemberDTO> getCurrentUser() {
        MemberDTO currentMember = marathonService.getCurrentMember();
        if (currentMember != null) {
            return ResponseEntity.ok(currentMember);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/payment")
    public String payment(HttpServletRequest request,
                          Model model,
                          Authentication authentication) {

        // 이전 페이지에서 전달된 데이터 받아오기
        Long marathonId = Long.parseLong(request.getParameter("marathonId"));
        Long marathonCourseId = Long.parseLong(request.getParameter("marathonCourseId"));
        String reward = request.getParameter("reward");

        // 현재 로그인한 사용자 가져오기
        String username = authentication.getName();
        // 회원정보 모델에 추가
        MemberDTO member = memberService.getMember(username);
        model.addAttribute("member", member);

        // 마라톤 이름 가져오기
        MarathonDTO marathon = marathonService.getMarathonById(marathonId);
        model.addAttribute("marathonName", marathon.getMarathonName());

        // 코스 데이터 가져오기
        MarathonCourseDTO marathonCourse = marathonService.getMarathonCourseById(marathonCourseId);
        model.addAttribute("course", marathonCourse.getCourse());
        model.addAttribute("price", marathonCourse.getPrice());

        // 데이터를 모델에 추가하여 결제 페이지로 전달
        model.addAttribute("marathonId", marathonId);
        model.addAttribute("marathonCourseId", marathonCourseId);
        model.addAttribute("reward", reward);
        model.addAttribute("username", username);

        // marathonJoin 테이블에 저장
        marathonService.saveMarathonJoin(marathonId, marathonCourseId, username, reward);

        return "marathon/payment";
    }


    @GetMapping("/getMemberInfo")
    @ResponseBody
    public String getMemberInfo(@RequestParam String username) {
        // 사용자 이름을 기반으로 사용자를 찾음
        MemberEntity member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));

        // 사용자 정보를 JSON 형식으로 반환
        return "{" +
                "\"email\":\"" + member.getMemberEmail() + "\"," +
                "\"memberTel\":\"" + member.getMemberTel() + "\"," +
                "\"memberAddr\":\"" + member.getMemberAddr() + "\"," +
                "\"memberPostCode\":\"" + member.getMemberPostCode() + "\"" +
                "}";
    }


    @GetMapping("/payment")
    public String doPaymentPage(@RequestParam("marathonId") Long marathonId,
                                @RequestParam("marathonCourseId") Long marathonCourseId,
                                @RequestParam("reward") String reward,
                                Model model) {

        // marathonId를 모델에 추가
        model.addAttribute("marathonId", marathonId);
        //리워드도 추가
        model.addAttribute("reward", reward);

        // 코스 데이터 넘겨서 가져오기
        MarathonCourseDTO marathonCourse = marathonService.getMarathonCourseById(marathonCourseId);
        model.addAttribute("course", marathonCourse.getCourse());
        model.addAttribute("price", marathonCourse.getPrice());

        // 마라톤 이름 가져오기
        MarathonDTO marathon = marathonService.getMarathonById(marathonId);
        model.addAttribute("marathonName", marathon.getMarathonName());
        model.addAttribute("marathonContent", marathon.getMarathonContent());
        model.addAttribute("marathonDate", marathon.getMarathonDate());
        return "marathon/payment";
    }


    @GetMapping("/currentMarathonJoinId")
    public ResponseEntity<Long> getCurrentMarathonJoinId(Authentication authentication) {
        // 현재 로그인한 사용자의 아이디를 가져옵니다.
        String username = authentication.getName();
        // 회원 정보를 가져옵니다.
        MemberDTO member = memberService.getMember(username);
        // 회원의 memberId 값을 가져옵니다.
        Long memberId = member.getId();
        // 현재 회원이 참가한 마라톤의 JoinId를 가져옵니다.
        Long marathonJoinId = marathonService.getMarathonJoinIdByMemberId(memberId);
        if (marathonJoinId != null) {
            return ResponseEntity.ok(marathonJoinId);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // 데이터 넘기기~
    @PostMapping("/complete")
    public String paymentCompletePost(// @RequestParam("marathonCourseId") Long marathonCourseId,
                                      // @RequestParam("reward") String reward,
                                      Model model) {
        // 데이터를 저장합니다.
        // model.addAttribute("marathonCourseId", marathonCourseId);
        // model.addAttribute("reward", reward);


        return "marathon/payComplete";
    }

    @GetMapping("/complete")
    public String paymentComplete(@RequestParam("marathonId") Long marathonId,
                                  //@RequestParam("marathonCourseId") Long marathonCourseId,
                                  //@RequestParam("marathonJoinId") Long marathonJoinId,
                                  Model model) {

        // 마라톤조인 넘겨서 가져오기
//        MarathonJoinDTO marathonJoin = marathonService.getMarathonJoinById(marathonJoinId);
//        model.addAttribute("marathonJoinId", marathonJoinId);
//        model.addAttribute("reward", marathonJoin.getReward());

        // 코스 데이터 넘겨서 가져오기
//        MarathonCourseDTO marathonCourse = marathonService.getMarathonCourseById(marathonCourseId);
//        model.addAttribute("course", marathonCourse.getCourse());
//        model.addAttribute("price", marathonCourse.getPrice());

        // 마라톤 이름 가져오기
        MarathonDTO marathon = marathonService.getMarathonById(marathonId);
        model.addAttribute("marathonId", marathon.getMarathonId());
        model.addAttribute("marathonName", marathon.getMarathonName());
//         model.addAttribute("marathonContent", marathon.getMarathonContent());
//         model.addAttribute("marathonDate", marathon.getMarathonDate());

        return "marathon/payComplete";
    }

    // 결제 취소 시 데이터 삭제
    @PostMapping("/cancelPayment")
    public String cancelPayment(@RequestParam("username") String username) {
        // 사용자 이름(username)을 기반으로 사용자 ID를 조회합니다.
        Long memberId = memberService.findMemberIdByUsername(username);
        // 사용자 ID를 기반으로 해당 사용자의 결제 정보를 조회하고 삭제합니다.
        marathonService.deleteMarathonJoin(memberId);
        // 취소가 성공적으로 이루어졌음을 클라이언트에게 응답합니다.
        return "member/main";
    }
    // 마라톤신청취소(cancel true)
    // @PostMapping("/cancelMarathon")
//    public String cancelMarathon(@RequestParam("marathonJoinId") Long marathonJoinId, Model model) {
//        marathonService.marathonJoinCancelById(marathonJoinId);
//        model.addAttribute("marathonJoinId", marathonJoinId);
//        return "main";
//    }
    // 마라톤신청취소(cancel true)
    @PostMapping("/cancelMarathon")
    public String cancelMarathonDel(@RequestParam("marathonJoinId") Long marathonJoinId, Model model) {
        marathonService.deleteCancelMarathon(marathonJoinId);
        return "member/main";
    }
}
