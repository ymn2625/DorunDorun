package com.example.dorundorun.service;

import com.example.dorundorun.dto.SMSDTO;
import com.example.dorundorun.entity.SMSEntity;
import com.example.dorundorun.repository.SMSRepository;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SMSService {

    private final DefaultMessageService defaultMessageService;
    private final SMSRepository smsRepository;

    public SMSService(SMSRepository smsRepository){
        this.defaultMessageService = NurigoApp.INSTANCE.initialize("NCSG9NRIB9QFA0UO", "TBP4ILOFSNHU4BAN2JQ68OGINCS0K3M6", "https://api.coolsms.co.kr");
        this.smsRepository = smsRepository;
    }


    public void sendCertification(SMSDTO smsdto) {

        String code = String.valueOf((int)Math.floor(Math.random()*900000)+100000);
        String tel = smsdto.getTel();

        System.out.println(code + tel+"여기는 오나?");

        Message message = new Message();

        message.setFrom("01051188412");
        message.setTo(tel);
        message.setText("[DorunDorun] 인증번호는 "+code+"입니다.");

        SingleMessageSentResponse response = this.defaultMessageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);

        SMSEntity smsEntity = SMSEntity.toSaveSMSEntity(smsdto);
        smsEntity.setCertificationCode(code);

        String certificationCode = smsEntity.getCertificationCode();
        String tel1 = smsEntity.getTel();
        System.out.println(certificationCode+tel1);

        smsRepository.save(smsEntity);

    }


    @Transactional
    public Integer verifyCertification(SMSDTO smsdto) {
        SMSEntity smsEntity = SMSEntity.toSaveSMSEntity(smsdto);
        System.out.println(smsEntity.getCertificationCode()+smsEntity.getTel());
        Optional<SMSEntity> byTelAndCertificationCode = smsRepository.findByTelAndCertificationCode(smsEntity.getTel(), smsEntity.getCertificationCode());
        smsRepository.deleteByTelAndCertificationCode(smsEntity.getTel(), smsEntity.getCertificationCode());
        if(byTelAndCertificationCode.isPresent()){
            return 1;
        }else{
            return 2;
        }

    }
}
