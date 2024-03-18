package com.example.dorundorun.controller;

import com.example.dorundorun.dto.MemberDTO;
import com.example.dorundorun.dto.RunningSpotDTO;
import com.example.dorundorun.entity.RunningSpotEntity;
import com.example.dorundorun.service.RunningSpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/place")
public class RunningSpotController {

    private final RunningSpotService runningSpotService;

    @GetMapping("/runningSpot")
    public String saveSpot(Model model) {
        List<RunningSpotEntity> saveRunningSpot = runningSpotService.getAllRunningSpots();
        List<MemberDTO> spotLocation = runningSpotService.getRunningSpot();
        model.addAttribute("spotLocation", spotLocation);
        model.addAttribute("saveRunningSpot", saveRunningSpot);
        return "runningSpot";
    }


}
