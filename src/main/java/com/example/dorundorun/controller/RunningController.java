package com.example.dorundorun.controller;

import com.example.dorundorun.dto.CrewMemberDTO;
import com.example.dorundorun.dto.RunningDTO;
import com.example.dorundorun.dto.RunningMemberDTO;
import com.example.dorundorun.dto.RunningSpotDTO;
import com.example.dorundorun.entity.RunningEntity;
import com.example.dorundorun.entity.RunningSpotEntity;
import com.example.dorundorun.repository.RunningSpotRepository;
import com.example.dorundorun.service.MemberService;
import com.example.dorundorun.service.RunningService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/running")
public class RunningController {

    private final MemberService memberService;
    private final RunningService runningService;
    private final RunningSpotRepository runningSpotRepository;

    // 홈
//    @GetMapping("/main")
//    public String runningHome(){
//        return "runningHome";
//    }

    //러닝일정 리스트
    @GetMapping("/list")
    public String runningList(Model model){
        List<RunningDTO> runningDTOList = runningService.findAllSortedByDate();
        List<String> spotNamesList = runningService.getSpotNamesForRunningList(runningDTOList);
        // 형식화된 날짜 리스트 생성
        List<String> formattedDates = formatDateList(runningDTOList);

        model.addAttribute("runningList", runningDTOList);
        model.addAttribute("spotNamesList", spotNamesList);
        model.addAttribute("formattedDates", formattedDates);

        return "runningList";
    }
    // 리스트 날짜 변환용
    private List<String> formatDateList(List<RunningDTO> runningDTOList) {
        List<String> formattedDates = new ArrayList<>();
        for (RunningDTO runningDTO : runningDTOList) {
            Date runningDate = runningDTO.getRunningDate();
            String formattedDate = formatDate(runningDate);
            formattedDates.add(formattedDate);
        }
        return formattedDates;
    }
    // 리스트 날짜 변환용
    private String formatDate(Date date) {
        // 날짜 형식 변환 로직
        if (date == null) {
            return "언제?";
        }
        Date currentDate = new Date();
        long diff = date.getTime() - currentDate.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);

        if (diffDays == 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return "오 늘 \n" + sdf.format(date);
        } else if (diffDays == 1) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return "내 일 \n" + sdf.format(date);
        } else if (diffDays == 2) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return "모 레 \n" + sdf.format(date);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd \n HH:mm");
            return sdf.format(date);
        }
    }


    //러닝일정 개설폼
    @GetMapping("/create")
    public String runningCreateForm(Model model){
        // 스팟리스트 부르기
        List<RunningSpotDTO> runningSpotDTOList = runningService.getAllRunningSpot();
        model.addAttribute("spotNames", runningSpotDTOList);
        return "runningCreate";
    }

    //러닝일정 만들
    @PostMapping("/create")
    public String runningCreate(@ModelAttribute RunningDTO runningDTO,
                                Authentication authentication,
                                @RequestParam("year") String year,
                                @RequestParam("month") String month,
                                @RequestParam("day") String day,
                                @RequestParam("hour") String hour,
                                @RequestParam("minute") String minute){

        try {
            String username = authentication.getName();
            String runningDateString = year + "-" + month + "-" + day + "T" + hour + ":" + minute + ":00";
            RunningDTO myRunning = runningService.runningCreate(runningDTO, username, runningDateString);
            Long runningId = myRunning.getRunningId();

            return "redirect:/running/detail/" + runningId;
        }catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

    }

    //러닝일정 상세보기
    @GetMapping("/detail/{id}")
    public String runningDetail(@PathVariable Long id, Model model, Authentication authentication){
        RunningDTO runningDTO = runningService.findById(id);
        int countRunningMember = runningService.countRunning(runningDTO);
        String username = authentication.getName();
        String nickname = memberService.getNickname(username);
        Boolean isMember = runningService.findByRunningDTO(runningDTO, username);
        List<RunningMemberDTO> runningMemberDTOList = runningService.runningMember(runningDTO);
        Boolean isAdmin = runningService.findByRunningMemberAuth(runningDTO, username);
        Optional<RunningSpotEntity> runningSpot = runningSpotRepository.findById(runningDTO.getSpotId());
        String spotName = runningSpot.map(RunningSpotEntity::getSpotName).orElse("Unknown");


        model.addAttribute("runningDTO", runningDTO);
        model.addAttribute("runningMember", countRunningMember);
        model.addAttribute("isMember", isMember);
        model.addAttribute("runningMemberList", runningMemberDTOList);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("memberNickname", nickname);
        model.addAttribute("spotName", spotName);

        return "runningDetail";
    }

    //러닝일정 신청
    @GetMapping("/join/{id}")
    public String runningJoin(@PathVariable Long id, Authentication authentication){
        String username = authentication.getName();

        runningService.joinRunning(username, id);

        return "redirect:/running/detail/"+id;
    }

    //러닝일정 수정으로 이동
    @GetMapping("/update/{id}")
    public String runningUpdate(@PathVariable Long id, Authentication authentication, Model model){
        RunningDTO runningDTO = runningService.findById(id);
        String username = authentication.getName();
        int countRunningMember = runningService.countRunning(runningDTO);
        model.addAttribute("runningMember", countRunningMember);
        Boolean isAdmin = runningService.findByRunningMemberAuth(runningDTO, username);
        List<RunningSpotDTO> runningSpotDTOList = runningService.getAllRunningSpot();
        model.addAttribute("spotNames", runningSpotDTOList);
        model.addAttribute("runningDate", runningDTO.getRunningDate());
        if(isAdmin){
            model.addAttribute("runningDTO", runningDTO);
            return "runningUpdate";
        }else {
            return "redirect:/running/detail/"+id;
        }
    }

    //러닝일정 수정
    @PostMapping("/update/{id}")
    public String runningUpdate(@PathVariable Long id,
                             @ModelAttribute RunningDTO runningDTO, Authentication authentication){
        runningDTO.setRunningId(id);
        String username = authentication.getName();
        Boolean isAdmin = runningService.findByRunningMemberAuth(runningDTO, username);
        if(isAdmin){
            runningService.update(runningDTO);
        }
        return "redirect:/running/detail/"+id;
    }

    //러닝일정 삭제
    @GetMapping("/delete/{id}")
    public String runningDelete(@PathVariable Long id, Authentication authentication){

        RunningDTO runningDTO = runningService.findById(id);
        String username = authentication.getName();
        Boolean isAdmin = runningService.findByRunningMemberAuth(runningDTO, username);
        if(isAdmin){
            runningService.deleteById(id);
        }

        return "redirect:/running/list";
    }

    //러닝일정 신청취소
    @GetMapping("/quitRunning/{id}")
    public String quitRunning(@PathVariable Long id, Authentication authentication){
        String username = authentication.getName();

        runningService.quitRunning(username, id);

        return "redirect:/running/detail/"+id;
    }

}
