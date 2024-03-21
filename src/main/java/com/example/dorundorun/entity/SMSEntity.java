package com.example.dorundorun.entity;

import com.example.dorundorun.dto.SMSDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "smsEntity")
public class SMSEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tel;

    private String certificationCode;

    public static SMSEntity toSaveSMSEntity(SMSDTO smsdto) {
        SMSEntity smsEntity = new SMSEntity();
        smsEntity.setTel(smsdto.getTel());
        smsEntity.setCertificationCode(smsdto.getCertificationCode());
        
        return smsEntity;
    }
}
