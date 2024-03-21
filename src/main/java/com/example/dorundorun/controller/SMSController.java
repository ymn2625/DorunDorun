package com.example.dorundorun.controller;

import com.example.dorundorun.dto.SMSDTO;
import com.example.dorundorun.service.SMSService;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sms")
public class SMSController {


    private final SMSService smsService;

    @PostMapping("/send")
    public ResponseEntity sendCertification (@ModelAttribute SMSDTO smsdto) {
        System.out.println(smsdto.getTel()+"전화보노보노");
        smsService.sendCertification(smsdto);
        return new ResponseEntity<>(smsdto, HttpStatus.OK);
    }

    @PostMapping("/verify")
    public ResponseEntity verifyCertification (@ModelAttribute SMSDTO smsdto) {
        int verify = smsService.verifyCertification(smsdto);
        if(verify==1) {
            return new ResponseEntity<>("인증성공", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("인증실패", HttpStatus.OK);
        }
    }

}
